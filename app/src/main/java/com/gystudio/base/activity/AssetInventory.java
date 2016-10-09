package com.gystudio.base.activity;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.gystudio.ksoap2.serialization.SoapPrimitive;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.gystudio.base.R;
import com.gystudio.base.entity.AppContext;
import com.gystudio.base.widget.AssetListViewAdapter;
import com.gystudio.db.DataHelper;
import com.gystudio.db.DataManager;
import com.gystudio.service.SOAPWebServiceTask;
import com.gystudio.utils.Alert;
import com.gystudio.utils.SysConfig;
import com.gystudio.utils.TranUtils;
import com.gystudio.widget.GyBaseActivity;
import com.gystudio.ws.DateSerializerUtils;
import com.gystudio.zxing.CaptureActivity;
import com.rstco.assetmgr.AssetEntity;
import com.rstco.sjpt.entity.PubOrganEntity;

/**
 */
public class AssetInventory extends GyBaseActivity<DataHelper> {
    private final static int REQUEST_CODE = 1;
    public static final String BARCODE = "barCode";
    public static final String BATCHNUM = "batchNum";

    private Spinner organSpinner = null;
    private AssetListViewAdapter assetListViewAdapter = null;

    private DataManager dataManager = new DataManager();

    private ArrayAdapter<PubOrganEntity> organSpinnerArrayAdapter = null;
    private ListView assetListView = null;
    private Button btnEnd = null;
    private Button btnScan = null;
    private Button btnIvtInfo = null;
    private Button btnManual = null;
    private Intent in = null;

    private String addr = AppContext.getAddress();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fs_serter_search_main);
        setTitle("资产盘点");
        Initialization();

    }

    private void Initialization() {

        in = getIntent();
        organSpinner = (Spinner) findViewById(R.id.asset_pd_organs);
        // TODO Auto-generated method stub
        //定义适配器

        organSpinnerArrayAdapter = new ArrayAdapter<PubOrganEntity>(this, android.R.layout.simple_spinner_item);
        organSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        organSpinner.setPrompt("选择使用部门");
        organSpinner.setAdapter(organSpinnerArrayAdapter);
        organSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                assetListViewAdapter.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //organcode=obj[0];
                // organSpinner.setSelection(0);
                // arg0.setVisibility(View.VISIBLE);
            }
        });

        try {
            List<PubOrganEntity> orglst = dataManager.findOrgans(this.getHelper().getOrgDao(), AppContext.currUser.getAccounts(), null);

            if (orglst.isEmpty()) {
                organSpinnerArrayAdapter.add(AppContext.currOrgan);
            } else {
                for (PubOrganEntity pubOrganEntity : orglst) {
                    organSpinnerArrayAdapter.add(pubOrganEntity);
                    System.out.println("add=====>" + pubOrganEntity.toString());
                }
            }

        } catch (Exception ex) {
            Toast.makeText(AssetInventory.this, "查询数据出错！" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        assetListView = (ListView) findViewById(R.id.asset_pd_list);

        assetListView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
        assetListView.setLongClickable(true);
        assetListView.setClickable(true);
        assetListView.setFastScrollEnabled(true);
        assetListView.setSelected(true);
        assetListView.setLongClickable(true);

        assetListViewAdapter = new AssetListViewAdapter(this, new ArrayList<AssetEntity>());
        assetListView.setAdapter(assetListViewAdapter);

        btnEnd = (Button) findViewById(R.id.fs_serter_search_main_btn_end);
        btnEnd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                organSpinner.setEnabled(true);
            }
        });

        btnIvtInfo = (Button) findViewById(R.id.fs_serter_search_main_btn_inventory);
        btnIvtInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inIvt = new Intent(AssetInventory.this,
                        InventorySearch.class);
                AssetInventory.this.startActivity(inIvt);
            }
        });

        btnManual = (Button) findViewById(R.id.fs_serter_search_main_btn_manaul);
        btnManual.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inIvt = new Intent(AssetInventory.this,
                        AssetManual.class);
                AssetInventory.this.startActivityForResult(inIvt, REQUEST_CODE);
            }
        });

        btnScan = (Button) findViewById(R.id.fs_serter_search_main_btn_scan);
        btnScan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (organSpinnerArrayAdapter.getCount() > 0) {
                    if (addr == null || addr.equals("")) {
                        Toast.makeText(AssetInventory.this, "未获得当前位置", Toast.LENGTH_LONG).show();
                    } else {
                        organSpinner.setEnabled(false);
                        Intent inScaning = new Intent(AssetInventory.this,
                                CaptureActivity.class);
                        AssetInventory.this.startActivityForResult(inScaning, REQUEST_CODE);
                    }
                } else {
                    Toast.makeText(AssetInventory.this, "没有部门资产可盘点", Toast.LENGTH_LONG).show();
                }
            }

        });
        isInventory();
        if (organSpinnerArrayAdapter.getCount() > 0) {
            organSpinner.performClick();
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        //super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(AssetInventory.this, " 返回    requestCode==>" +requestCode
        //		+ "  结果==>"+resultCode, Toast.LENGTH_LONG).show();

        if (resultCode == CaptureActivity.RESULT_CODE) {
            Bundle bundle = data.getExtras();
            String barcode = bundle.getString("barcode");
            if (!"".equals(barcode)) {
                String[] codes = SysConfig.getCodes(barcode);
                selectAssetInfo(codes[0], "1");
            }
        } else if (resultCode == AssetManual.RESULT_CODE) {
            Bundle bundle = data.getExtras();
            String code = bundle.getString("code");
            String name = bundle.getString("name");
            if (StringUtils.isNotBlank(code)) {
                selectAssetInfo(code, "2");
            }
        } else if (resultCode == InventoryChgSel.RESULT_CODE) {
            Bundle bundle = data.getExtras();
            AssetEntity asset = (AssetEntity) bundle.get("asset");
            if (null != asset) {
                doInventory(asset);
                //assetListViewAdapter.notifyDataSetChanged();
            }
        }
    }

    private void isInventory() {
        String serviceUrl = SysConfig.getServiceUrl();// WebSerivce地址e
        String nameSpace = SysConfig.getNameSpace(); // 空间名,可修改
        String methodName = "isInventory"; // 需调用WebService名称

        HashMap<String, String> hashMap = new LinkedHashMap<String, String>();

        hashMap.put("organCode", AppContext.currOrgan.getOrganCode());

        System.out.println("serviceUrl==>" + serviceUrl);

        try {
            new SOAPWebServiceTask(this, "正在加载...") {
                @Override
                public boolean handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            onIsInventoryData((SoapPrimitive) msg.obj);
                            break;
                        case 2:
                            Alert.DisplayAlertDialog(AssetInventory.this, "提示", "远程服务器无响应！",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface paramDialogInterface,
                                                int paramInt) {
                                            finish();
                                        }

                                    });
                            break;
                    }
                    return false;
                }
            }.execute(serviceUrl, nameSpace, methodName, hashMap);
        } catch (Exception e) {
            Toast.makeText(AssetInventory.this, "请求出错！" + e.getMessage(), Toast.LENGTH_LONG).show();
            Alert.DisplayProgressDialogCancel();
            finish();
        }
    }

    public void onIsInventoryData(SoapPrimitive soapObject) {

        try {
            JsonObject jsonObject = TranUtils.getJsonObject(soapObject);

            Log.e("JsonData", soapObject.toString());
            Log.e("success", jsonObject.get("success").getAsString());
            int success = jsonObject.get("success").getAsInt();
            String message = jsonObject.get("message").getAsString();

            if (success != 1) {
                Alert.DisplayAlertQqDialog(AssetInventory.this, 1, "提示", message, "确定", new OnClickListener() {

                    @Override
                    public void onClick(View paramView) {
                        // TODO Auto-generated method stub
                        finish();
                    }

                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    private void selectAssetInfo(String assetCode, final String pdfs) {
        String serviceUrl = SysConfig.getServiceUrl();// WebSerivce地址e
        String nameSpace = SysConfig.getNameSpace(); // 空间名,可修改
        String methodName = "getAssetInfoWithInv"; // 需调用WebService名称

        HashMap<String, String> hashMap = new LinkedHashMap<String, String>();
        hashMap.put("assetCode", assetCode);
        Log.i("", assetCode);
        System.out.println("serviceUrl==>" + serviceUrl);
        try {
            new SOAPWebServiceTask(this, "正在加载...") {
                @Override
                public boolean handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            onAssetInfoLoadData((SoapPrimitive) msg.obj, pdfs);
                            break;
                        case 2:
                            Toast.makeText(AssetInventory.this, "远程服务器无响应！", Toast.LENGTH_LONG).show();
                            break;
                    }
                    return false;
                }
            }.execute(serviceUrl, nameSpace, methodName, hashMap);
        } catch (Exception e) {
            Toast.makeText(AssetInventory.this, "请求出错！" + e.getMessage(), Toast.LENGTH_LONG).show();
            Alert.DisplayProgressDialogCancel();
        }
    }

    public void onAssetInfoLoadData(SoapPrimitive soapObject, String pdfs) {

        try {
            String json = TranUtils.decode(soapObject.toString());

            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(json);

            Log.e("JsonData", soapObject.toString());
            Log.e("success", jsonObject.get("success").getAsString());
            int success = jsonObject.get("success").getAsInt();
            String message = jsonObject.get("message").getAsString();

            if (success == 1) {
                //String ustr = jsonObject.get("user")
                Gson gson = new Gson();
                final AssetEntity asset = gson.fromJson(jsonObject.get("asset"), AssetEntity.class);
                Log.e("asset ", asset.toString());
                StringBuffer sb = new StringBuffer();
                sb.append("资产编码：").append(StringUtils.isNotBlank(asset.getFinCode()) ? asset.getFinCode() : asset.getAssetCode()).append("\n");
                sb.append("资产名称：").append(asset.getAssetName()).append("\n");
                sb.append("资产类型：").append(asset.getAssetTypeName()).append("\n");
                sb.append("资产类别：").append(asset.getCateName()).append("\n");
                //sb.append("财务编码：").append(asset.getFinCode()).append("\n");
                sb.append("管理部门：").append(asset.getMgrOrganName()).append("\n");
                sb.append("使用部门：").append(asset.getOrganName()).append("\n");
                sb.append("存放地点：").append(asset.getStorageDescr()).append("\n");
                sb.append("使  用  人：").append(asset.getOperator()).append("\n");
                sb.append("原　　值：").append(asset.getOriginalValue()).append("\n");
                sb.append("使用日期：").append(asset.getEnableDateString()).append("\n");
                sb.append("使用年限：").append(asset.getUseAge()).append("\n");
                sb.append("当前状态：").append(asset.getStatus());

                String invMsg = jsonObject.get("invMsg").getAsString();
                Float starnum = asset.getStarNum() == null ? null : asset.getStarNum().floatValue();
                if (StringUtils.isNotBlank(invMsg)) {
                    sb.append("\n").append(invMsg);
                }
                message = sb.toString();
                asset.setDisCodes("");
                asset.setPdfs(pdfs);
                Alert.DisplayAlertAssetInfoDialog(this, 0, "信息", starnum, message, "确认盘点",
                        "不符项目", new OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                doInventory(asset);
                            }
                        }, new OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                Intent inSeling = new Intent(AssetInventory.this,
                                        InventoryChgSel.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("asset", asset);
                                inSeling.putExtras(bundle);
                                AssetInventory.this.startActivityForResult(inSeling, REQUEST_CODE);
                            }
                        });
            } else {
                Alert.DisplayAlertQqDialog(this, 0, "信息", message, "继续", "中止",
                        new OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                Intent inScaning = new Intent(
                                        AssetInventory.this,
                                        CaptureActivity.class);
                                AssetInventory.this.startActivityForResult(
                                        inScaning, REQUEST_CODE);
                            }
                        }, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public String getAssetJson(AssetEntity obj) {
        String json = "";
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(java.util.Date.class,
                new DateSerializerUtils()).setDateFormat(DateFormat.LONG);

        gsonBuilder.registerTypeAdapter(Double.class,
                new JsonDeserializer<Double>() {
                    public Double deserialize(JsonElement json, Type typeOfT,
                                              JsonDeserializationContext context)
                            throws JsonParseException {
                        try {
                            return Double.valueOf(json.getAsString());
                        } catch (Exception e) {
                            return null;
                        }
                    }
                });
        Gson g = gsonBuilder.create();
        json = g.toJson(obj);
        return json;
    }

    private void doInventory(final AssetEntity asset) {
        String serviceUrl = SysConfig.getServiceUrl();// WebSerivce地址e
        String nameSpace = SysConfig.getNameSpace(); // 空间名,可修改
        String methodName = "doInventory"; // 需调用WebService名称

        HashMap<String, String> hashMap = new LinkedHashMap<String, String>();


        asset.setSimId(AppContext.getSimId());
        asset.setAddr(addr);
        asset.setMgrOrganCode(AppContext.currOrgan.getOrganCode());
        asset.setOrganCode(null != organSpinner.getSelectedItem() ? ((PubOrganEntity) organSpinner.getSelectedItem()).getOrganCode() : "");


        hashMap.put("username", AppContext.currUser.getAccounts());
        hashMap.put("assetJson", this.getAssetJson(asset));

        Log.i("", asset.getAssetCode());

        System.out.println("serviceUrl==>" + serviceUrl);

        try {
            new SOAPWebServiceTask(this, "正在加载...") {
                @Override
                public boolean handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            onInventoryLoadData((SoapPrimitive) msg.obj);
                            break;
                        case 2:
                            Toast.makeText(AssetInventory.this, "远程服务器无响应！", Toast.LENGTH_LONG).show();
                            break;
                    }
                    return false;
                }
            }.execute(serviceUrl, nameSpace, methodName, hashMap);
        } catch (Exception e) {
            Toast.makeText(AssetInventory.this, "请求出错！" + e.getMessage(), Toast.LENGTH_LONG).show();
            Alert.DisplayProgressDialogCancel();
        }
    }

    public void onInventoryLoadData(SoapPrimitive soapObject) {

        try {
            String json = TranUtils.decode(soapObject.toString());

            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(json);

            Log.e("JsonData", soapObject.toString());
            Log.e("success", jsonObject.get("success").getAsString());
            int success = jsonObject.get("success").getAsInt();
            String message = jsonObject.get("message").getAsString();

            if (success == 1) {
                //String ustr = jsonObject.get("user")
                Gson gson = new Gson();
                final AssetEntity asset = gson.fromJson(jsonObject.get("asset"), AssetEntity.class);
                message = "[" + asset.getAssetCode() + "] " + asset.getAssetName() + " 盘点成功";
                assetListViewAdapter.replace(asset);
            }
            Alert.DisplayAlertQqDialog(this, 0, "信息", message, "继续", "中止",
                    new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            Intent inScaning = new Intent(AssetInventory.this,
                                    CaptureActivity.class);
                            AssetInventory.this.startActivityForResult(inScaning, REQUEST_CODE);
                        }
                    }, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 0, 0, "添加测试数据").setIcon(R.drawable.menu_plus);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
