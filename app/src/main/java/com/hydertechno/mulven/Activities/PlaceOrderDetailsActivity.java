package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Adapters.OrderItemsAdapter;
import com.hydertechno.mulven.Adapters.OrderTimelineAdapter;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.Fragments.LoadingDialog;
import com.hydertechno.mulven.Internet.Connection;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.CancellationReasonModel;
import com.hydertechno.mulven.Models.CartProductModel;
import com.hydertechno.mulven.Models.InvoiceDetailsModel;
import com.hydertechno.mulven.Models.OrderDetails;
import com.hydertechno.mulven.Models.OrderItemsModel;
import com.hydertechno.mulven.Models.OrderTimelineModel;
import com.hydertechno.mulven.Models.RequiredDataModel;
import com.hydertechno.mulven.Models.ShurjoPayPaymentModel;
import com.hydertechno.mulven.Models.UserProfile;
import com.hydertechno.mulven.R;
import com.sm.shurjopaysdk.listener.PaymentResultListener;
import com.sm.shurjopaysdk.model.TransactionInfo;
import com.sm.shurjopaysdk.payment.ShurjoPaySDK;
import com.sm.shurjopaysdk.utils.SPayConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceOrderDetailsActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener {
    private TextView invoiceIdTV, orderTimeTV, vendorNameTV, vendorPhoneTV, vendorAddressTV, customerNameTV,
            customerPhoneTV, customerAddressTV, customerAddressEditTV, totalPaidTV,orderStatusTV,reportIssueTV,existingIssueTV;
    public static TextView totalPriceTv, dueTV,makePaymentTV;
    public static double totalPay;
    private Dialog cancelledDialog, makePaymentDialog;
    private RatingBar ratingBar;
    private String token, OrderId,paymentOrderStatus,orderStatus;
    private int userId;
    private ImageView vendorImageIV, customerImageIV, moreIcon,deliveredIcon;
    private SharedPreferences sharedPreferences;
    private List<InvoiceDetailsModel> invoiceDetailsModelList;
    private FrameLayout frame_layout2;
    private RelativeLayout topRelative,descriptionLinearLayout,statusRelative,oderTimeLineRL,refundRelative;
    private LinearLayout priceLinearLayout;
    private RecyclerView timelineRecyclerView, orderItemListRecyclerView;
    private RelativeLayout rootLayout;
    private Snackbar snackbar;
    private PopupMenu popup;
    private LoadingDialog loadingDialog;
    private boolean isCampaignAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_details);
        loadingDialog = LoadingDialog.instance();
        init();
        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        }
        Intent intent = getIntent();
        OrderId = intent.getStringExtra("OrderId");
        paymentOrderStatus=intent.getStringExtra("PaymentStatus");
        orderStatusTV.setText(paymentOrderStatus);
        getInvoiceDetails();
        makePaymentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                }else{
                    String amount=dueTV.getText().toString().substring(2);
                    makePaymentDialog = new Dialog(PlaceOrderDetailsActivity.this);
                    makePaymentDialog.setContentView(R.layout.make_payment_layout_design);
                    makePaymentDialog.setCancelable(false);
                    makePaymentDialog.show();
                    Window window = makePaymentDialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    EditText paymentAmount= makePaymentDialog.findViewById(R.id.makePayET);
                    TextView continuePayTV= makePaymentDialog.findViewById(R.id.continuePayTV);
                    TextView closePayTV= makePaymentDialog.findViewById(R.id.closePayTV);
                    paymentAmount.setHint(amount);
                    closePayTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            makePaymentDialog.dismiss();
                        }
                    });

                    continuePayTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                String am=paymentAmount.getText().toString();
                                if(!TextUtils.isEmpty(am)){
                                    try {
                                        double value=Double.parseDouble(am);
                                        if(value>0){
                                            Intent intent = new Intent(PlaceOrderDetailsActivity.this, PaymentMethodsActivity.class);
                                            intent.putExtra("amount", value);
                                            intent.putExtra("FAmount", dueTV.getText().toString());
                                            intent.putExtra("orderId", OrderId);
                                            intent.putExtra("isCampaign", isCampaignAvailable);
                                            startActivity(intent);
                                            makePaymentDialog.dismiss();
                                        }else
                                            Toast.makeText(PlaceOrderDetailsActivity.this, "Amount can not 0", Toast.LENGTH_SHORT).show();

                                    }catch (Exception e){
                                        Toast.makeText(PlaceOrderDetailsActivity.this, "Amount must be number", Toast.LENGTH_SHORT).show();
                                    }


                                }else{
                                    Toast.makeText(PlaceOrderDetailsActivity.this, "Please enter your amount", Toast.LENGTH_SHORT).show();
                                }

                            }
                    });

                }
            }
        });
        moreIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected) {
                    snackBar(isConnected);
                }else{
                    popup = new PopupMenu(PlaceOrderDetailsActivity.this, moreIcon);
                    popup.setOnMenuItemClickListener(PlaceOrderDetailsActivity.this);
                    popup.getMenuInflater().inflate(R.menu.cancel_product_menu, popup.getMenu());
                    if(orderStatus.equals("Pending") || orderStatus.equals("Partial Paid")){
                        popup.getMenu().removeItem(R.id.deliveredOrder);
                    } else if(orderStatus.equals("Shipped")){
                    popup.getMenu().removeItem(R.id.cancelOrder);
                }
                    popup.show();

                }
            }
        });

        Call<UserProfile> call = ApiUtils.getUserService().getUserData(token);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()) {
                    customerNameTV.setText(response.body().getFull_name());
                    customerPhoneTV.setText(response.body().getPhone());
                    if (response.body().getUser_photo() != null) {
                        try {
                            Picasso.get()
                                    .load(Config.IMAGE_LINE + response.body().getUser_photo())
                                    .into(customerImageIV);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {

            }
        });

        reportIssueTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("orderId", OrderId);
                ReportIssueBottomSheet bottom_sheet = new ReportIssueBottomSheet();
                bottom_sheet.setArguments(args);
                bottom_sheet.show(getSupportFragmentManager(), "bottomSheet");
            }
        });
        existingIssueTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlaceOrderDetailsActivity.this, ExistingIssueActivity.class);
                startActivity(intent);
            }
        });

        customerAddressEditTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelledDialog = new Dialog(PlaceOrderDetailsActivity.this);
                cancelledDialog.setContentView(R.layout.edit_address_popup_design);
                cancelledDialog.setCancelable(true);
                cancelledDialog.show();
                Window window = cancelledDialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                EditText delivery_ET = cancelledDialog.findViewById(R.id.delivery_ET);
                TextView saveAddressTV = cancelledDialog.findViewById(R.id.saveAddressTV);
                delivery_ET.setText(customerAddressTV.getText());
                saveAddressTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkConnection();
                        if (!isConnected) {
                            snackBar(isConnected);
                            cancelledDialog.dismiss();
                        }else{
                        String address = delivery_ET.getText().toString();
                        if (TextUtils.isEmpty(address)) {
                            Toast.makeText(PlaceOrderDetailsActivity.this, "Please provide delivery address!", Toast.LENGTH_LONG).show();
                        } else {
                            Call<OrderDetails> call = ApiUtils.getUserService().updateDeliverAddress(OrderId, token, address);
                            call.enqueue(new Callback<OrderDetails>() {
                                @Override
                                public void onResponse(Call<OrderDetails> call, Response<OrderDetails> response) {
                                    if (response.isSuccessful()) {
                                        String status = response.body().getStatus();
                                        if (status.equals("1")) {
                                            Toast.makeText(PlaceOrderDetailsActivity.this, "Delivery address updated!", Toast.LENGTH_SHORT).show();
                                            getInvoiceDetails();
                                            cancelledDialog.dismiss();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<OrderDetails> call, Throwable t) {

                                }
                            });
                        }
                    }
                    }
                });
            }
        });
    }



    private void init() {
        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
        userId =sharedPreferences.getInt("userId",0);
        rootLayout = findViewById(R.id.place_order_details_rootLayout);
        frame_layout2 = findViewById(R.id.frame_layout2);
        topRelative = findViewById(R.id.topRelative);
        descriptionLinearLayout = findViewById(R.id.descriptionLinearLayout);
        statusRelative = findViewById(R.id.statusRelative);
        oderTimeLineRL = findViewById(R.id.oderTimeLineRL);
        refundRelative = findViewById(R.id.refundRelative);
        priceLinearLayout = findViewById(R.id.priceLinearLayout);

        invoiceIdTV = findViewById(R.id.InvoiceTV);
        moreIcon = findViewById(R.id.moreIcon);
        orderTimeTV = findViewById(R.id.orderTimeTV);
        vendorNameTV = findViewById(R.id.vendorNameTV);
        vendorImageIV = findViewById(R.id.vendorImageTV);
        vendorPhoneTV = findViewById(R.id.vendorPhoneTV);
        vendorAddressTV = findViewById(R.id.vendorAddressTV);
        customerNameTV = findViewById(R.id.customerNameTV);
        customerImageIV = findViewById(R.id.customerImageTV);
        customerPhoneTV = findViewById(R.id.customerPhoneTV);
        customerAddressTV = findViewById(R.id.customerAddressTV);
        customerAddressEditTV = findViewById(R.id.customerAddressEditTV);
        reportIssueTV = findViewById(R.id.reportIssueTV);
        existingIssueTV = findViewById(R.id.existingIssueTV);
        totalPaidTV = findViewById(R.id.totalPaidTV);
        totalPriceTv = findViewById(R.id.totalPriceTv);
        orderStatusTV = findViewById(R.id.orderStatusTV);
        makePaymentTV = findViewById(R.id.makePaymentTV);
        paymentOrderStatus=orderStatusTV.getText().toString();

        dueTV = findViewById(R.id.dueTV);
        ratingBar = findViewById(R.id.ratingBar);
        timelineRecyclerView = findViewById(R.id.timelineRecyclerView);
        orderItemListRecyclerView = findViewById(R.id.orderItemListRecyclerView);
    }


    private void getInvoiceDetails() {
        if (!loadingDialog.isAdded())
            loadingDialog.show(getSupportFragmentManager(), null);

        invoiceIdTV.setText(OrderId);
        Call<InvoiceDetailsModel> call = ApiUtils.getUserService().getInvoiceDetails(OrderId, token);
        call.enqueue(new Callback<InvoiceDetailsModel>() {
            @Override
            public void onResponse(Call<InvoiceDetailsModel> call, Response<InvoiceDetailsModel> response) {
                Log.e("Response ===> ", response.toString());
                loadingDialog.dismiss();
                topRelative.setVisibility(View.VISIBLE);
                descriptionLinearLayout.setVisibility(View.VISIBLE);
                statusRelative.setVisibility(View.VISIBLE);
                oderTimeLineRL.setVisibility(View.VISIBLE);
                refundRelative.setVisibility(View.VISIBLE);
                priceLinearLayout.setVisibility(View.VISIBLE);
                InvoiceDetailsModel details = response.body();
                String shopName = details.getOrderDetails().getShopName();
                String shopPhone = details.getOrderDetails().getSellerPhone();
                String shopAddress = details.getOrderDetails().getShopAddress();
                String shopImage = details.getOrderDetails().getShopLogo();
                String orderTime = details.getOrderDetails().getTime();
                String orderDate = details.getOrderDetails().getDate();
                String customerAddress = details.getOrderDetails().getDeliveryAddress();
                orderStatus = details.getOrderDetails().getOrdersStatus();
                vendorNameTV.setText(shopName);
                vendorPhoneTV.setText(shopPhone);
                vendorAddressTV.setText(shopAddress);
                orderTimeTV.setText(orderDate + " " + orderTime);
                totalPaidTV.setText("৳ " +details.getTotalPay());
                totalPay = details.getTotalPay();

                try {
                    Picasso.get()
                            .load(Config.IMAGE_LINE + shopImage)
                            .into(vendorImageIV);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(orderStatus.equals("Pending") || orderStatus.equals("Partial Paid") ||(orderStatus.equals("Shipped"))){
                    moreIcon.setVisibility(View.VISIBLE);
                   // popup.getMenu().removeItem(R.id.deliveredOrder);
                }
                /*if(orderStatus.equals("Shipped")){
                    moreIcon.setVisibility(View.VISIBLE);
                    popup.getMenu().removeItem(R.id.cancelOrder);
                }*/
                if(customerAddress==null){
                    Call<UserProfile> call2 = ApiUtils.getUserService().getUserData(token);
                    call2.enqueue(new Callback<UserProfile>() {
                        @Override
                        public void onResponse(Call<UserProfile> call2, Response<UserProfile> response) {
                            if (response.isSuccessful()) {
                                customerAddressTV.setText(response.body().getAddress());
                            }
                        }

                        @Override
                        public void onFailure(Call<UserProfile> call2, Throwable t) {
                        }
                    });
                }else{
                    customerAddressTV.setText(customerAddress);
                    customerAddressEditTV.setVisibility(View.GONE);
                }

                //Order Item
                List<OrderItemsModel> orderItemsModelList = response.body().getItems();

                OrderItemsAdapter orderItemsAdapter = new OrderItemsAdapter(orderItemsModelList, PlaceOrderDetailsActivity.this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(PlaceOrderDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                orderItemListRecyclerView.setLayoutManager(layoutManager);
                orderItemListRecyclerView.setAdapter(orderItemsAdapter);
                orderItemsAdapter.notifyDataSetChanged();

                isCampaignAvailable = getIsCampaignAvailable(orderItemsModelList);

                //Time Line
                List<OrderTimelineModel> orderTimelineModelList = details.getTimeline();
                OrderTimelineAdapter orderTimelineAdapter = new OrderTimelineAdapter(orderTimelineModelList, getApplicationContext());
                LinearLayoutManager layoutManager2 = new LinearLayoutManager(PlaceOrderDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                timelineRecyclerView.setLayoutManager(layoutManager2);
                timelineRecyclerView.setAdapter(orderTimelineAdapter);
               // Collections.reverse(orderTimelineModelList);
                orderTimelineAdapter.notifyDataSetChanged();


                switch (paymentOrderStatus) {
                    case "Unpaid":
                        orderStatusTV.setTextColor(Color.parseColor("#DB4437"));
                        if(orderStatus.equals("Cancel")){
                            makePaymentTV.setVisibility(View.GONE);
                        }else {
                            makePaymentTV.setVisibility(View.VISIBLE);
                        }
                        break;
                    case "Partial Paid":
                        orderStatusTV.setTextColor(Color.parseColor("#4285F4"));
                        if(orderStatus.equals("Cancel")){
                            makePaymentTV.setVisibility(View.GONE);
                        }else {
                            makePaymentTV.setVisibility(View.VISIBLE);
                        }
                        break;
                    case "Paid":
                        orderStatusTV.setTextColor(Color.parseColor("#DB4437"));
                        makePaymentTV.setVisibility(View.GONE);
                        break;
                }

            }

            @Override
            public void onFailure(Call<InvoiceDetailsModel> call, Throwable t) {

            }
        });
    }


    private boolean getIsCampaignAvailable(List<OrderItemsModel> products) {
        for (OrderItemsModel item : products) {
            if (item.getOrderFrom() != null && !item.getOrderFrom().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void placeOrderIdCopy(View view) {
        ClipboardManager cm = (ClipboardManager) PlaceOrderDetailsActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(invoiceIdTV.getText());
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.cancelOrder:
            //Toast.makeText(PlaceOrderDetailsActivity.this, "Item 1 Selected", Toast.LENGTH_SHORT).show();

            cancelledDialog = new Dialog(PlaceOrderDetailsActivity.this);
            cancelledDialog.setContentView(R.layout.cancel_reason_layout_design);
            cancelledDialog.setCancelable(true);
            TextView submitReasonTV= cancelledDialog.findViewById(R.id.submitReasonTV);
            CheckBox reason1,reason2,reason3,reason4,reason5,reason6;
            reason1= cancelledDialog.findViewById(R.id.reason1);
            reason2= cancelledDialog.findViewById(R.id.reason2);
            reason3= cancelledDialog.findViewById(R.id.reason3);
            reason4= cancelledDialog.findViewById(R.id.reason4);
            reason5= cancelledDialog.findViewById(R.id.reason5);
            reason6= cancelledDialog.findViewById(R.id.reason6);
            final String[] reason = new String[1];
            reason1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(reason1.isChecked()){
                        reason2.setChecked(false);
                        reason3.setChecked(false);
                        reason4.setChecked(false);
                        reason5.setChecked(false);
                        reason6.setChecked(false);
                        reason[0] =reason1.getText().toString();
                    }
                }
            });
            reason2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(reason2.isChecked()){
                        reason1.setChecked(false);
                        reason3.setChecked(false);
                        reason4.setChecked(false);
                        reason5.setChecked(false);
                        reason6.setChecked(false);
                        reason[0] =reason2.getText().toString();
                    }
                }
            });
            reason3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(reason3.isChecked()){
                        reason2.setChecked(false);
                        reason1.setChecked(false);
                        reason4.setChecked(false);
                        reason5.setChecked(false);
                        reason6.setChecked(false);
                        reason[0] =reason3.getText().toString();
                    }
                }
            });
            reason4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(reason4.isChecked()){
                        reason2.setChecked(false);
                        reason3.setChecked(false);
                        reason1.setChecked(false);
                        reason5.setChecked(false);
                        reason6.setChecked(false);
                        reason[0] =reason4.getText().toString();
                    }
                }
            });
            reason5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(reason5.isChecked()){
                        reason2.setChecked(false);
                        reason3.setChecked(false);
                        reason4.setChecked(false);
                        reason1.setChecked(false);
                        reason6.setChecked(false);
                        reason[0] =reason5.getText().toString();
                    }
                }
            });

            reason6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(reason6.isChecked()){
                        reason2.setChecked(false);
                        reason3.setChecked(false);
                        reason4.setChecked(false);
                        reason5.setChecked(false);
                        reason1.setChecked(false);
                        reason[0] =reason6.getText().toString();
                    }
                }
            });


            submitReasonTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkConnection();
                    if (!isConnected) {
                        snackBar(isConnected);
                        cancelledDialog.dismiss();
                    }else{
                    if (reason1.isChecked() || reason2.isChecked() || reason3.isChecked() || reason4.isChecked() || reason5.isChecked() || reason6.isChecked()) {
                        Call<CancellationReasonModel> call = ApiUtils.getUserService().setCancelReason(token, OrderId, reason[0]);
                        call.enqueue(new Callback<CancellationReasonModel>() {
                            @Override
                            public void onResponse(Call<CancellationReasonModel> call, Response<CancellationReasonModel> response) {
                                if (response.body().getStatus() == 1) {
                                    Toasty.normal(PlaceOrderDetailsActivity.this, "Order has been cancelled", Toasty.LENGTH_SHORT).show();

                                    recreate();
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("success", true);
                                    setResult(PlaceOrderListActivity.Place_Order_Request_Code ,resultIntent);
                                } else {
                                    Toasty.error(PlaceOrderDetailsActivity.this, "Something went wrong", Toasty.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<CancellationReasonModel> call, Throwable t) {
                                Toasty.error(PlaceOrderDetailsActivity.this, "Something went wrong", Toasty.LENGTH_SHORT).show();
                            }
                        });

                        cancelledDialog.dismiss();
                    } else {
                        Toasty.info(PlaceOrderDetailsActivity.this, "Please select your cancellation reason", Toasty.LENGTH_SHORT).show();
                    }
                }
            }
            });

            cancelledDialog.show();


            Window window = cancelledDialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            break;
            case R.id.deliveredOrder:
                androidx.appcompat.app.AlertDialog.Builder dialog2 = new androidx.appcompat.app.AlertDialog.Builder(this);
                dialog2.setTitle("Order Delivered");
                dialog2.setIcon(R.drawable.applogo);
                dialog2.setMessage("Do you get your delivery?");
                dialog2.setCancelable(false);
                dialog2.setPositiveButton("Yes I Get", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog2, int which) {
                        checkConnection();
                        if (!isConnected) {
                            snackBar(isConnected);
                            dialog2.dismiss();
                        }else{
                        Call<CancellationReasonModel> call = ApiUtils.getUserService().setOrderDelivered(token, OrderId);
                        call.enqueue(new Callback<CancellationReasonModel>() {
                            @Override
                            public void onResponse(Call<CancellationReasonModel> call, Response<CancellationReasonModel> response) {
                                if (response.body().getStatus() == 1) {
                                    Toasty.normal(PlaceOrderDetailsActivity.this, "Order has delivered.", Toasty.LENGTH_SHORT).show();
                                    recreate();
                                } else {
                                    Toasty.error(PlaceOrderDetailsActivity.this,
                                            "Confirm us by pressing the received button once you " +
                                                    "receive your product. This is the only proof that you" +
                                                    " got the product in hand", Toasty.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<CancellationReasonModel> call, Throwable t) {
                                Toasty.error(PlaceOrderDetailsActivity.this, "Something went wrong", Toasty.LENGTH_SHORT).show();
                            }
                        });
                        dialog2.dismiss();
                    }
                }
                });
                dialog2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog2, int which) {
                        dialog2.dismiss();
                    }
                });
                AlertDialog alertDialog2 = dialog2.create();
                alertDialog2.show();
                break;
        }
        return false;
    }


    public void placeOrderDetailsBack(View view) {
        Intent resultIntent = new Intent();
//        setResult(PlaceOrderListActivity.Place_Order_Request_Code ,resultIntent);
        //overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        snackBar(isConnected);
    }


    public void snackBar(boolean isConnected) {
        if(!isConnected) {
            snackbar = Snackbar.make(rootLayout, "No Internet Connection! Please Try Again.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setDuration(5000);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
        }
    }
}