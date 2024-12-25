package com.sth4me.capacitor.sumup;

import androidx.activity.result.ActivityResult;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.sumup.merchant.reader.api.SumUpAPI;
import com.sumup.merchant.reader.api.SumUpLogin;
import com.sumup.merchant.reader.api.SumUpPayment;
import com.sumup.merchant.reader.api.SumUpState;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Objects;

@CapacitorPlugin(name = "SumUpPlugin")
public class SumUpPlugin extends Plugin {

    private static final int REQUEST_CODE_LOGIN = 1;
    private static final int REQUEST_CODE_CHECKOUT = 2;
    private static final int REQUEST_CODE_CARD_READER_PAGE = 4;

    @Override
    public void load() {
        SumUpState.init(getContext());
    }


    @PluginMethod
    public void echo (PluginCall call) {
        String value = call.getString("value");
        call.resolve(new JSObject().put("value", value));
    }

    @PluginMethod
    public void login(PluginCall call) {
        bridge.saveCall(call);

        String affiliateKey = call.getData().getString("affiliateKey");

        assert affiliateKey != null;
        SumUpLogin.Builder sumLogin = SumUpLogin.builder(affiliateKey);

        if (call.getData().has("accessToken")) {
            String accessToken = call.getData().getString("accessToken");
            sumLogin.accessToken(accessToken);

        }
        SumUpAPI.openLoginActivity(getActivity(), sumLogin.build(), REQUEST_CODE_LOGIN);
    }


    @PluginMethod
    public void merchant(PluginCall call) {
        if (!SumUpAPI.isLoggedIn()) {
            call.reject("Result Code: " + SumUpAPI.Response.ResultCode.ERROR_NOT_LOGGED_IN);
            return;
        }
        String isoCode = Objects.requireNonNull(SumUpAPI.getCurrentMerchant()).getCurrency().getIsoCode();
        String merchantCode = SumUpAPI.getCurrentMerchant().getMerchantCode();
        call.resolve(new JSObject().put("isoCode", isoCode).put("merchantCode", merchantCode));
    }

    @PluginMethod
    public void checkout(PluginCall call) {
        bridge.saveCall(call);

        SumUpPayment.Builder payment = SumUpPayment.builder()
                .total(new BigDecimal(call.getData().getString("total")))
                .currency(SumUpPayment.Currency.valueOf(call.getData().getString("currency")));

        JSObject data = call.getData();

        if (data.has("title")) {
            String title = data.getString("title");
            payment.title(title);
        }

        if (data.has("tipOnCardReader") && SumUpAPI.isTipOnCardReaderAvailable()) {
            if (Boolean.TRUE.equals(data.getBoolean("tipOnCardReader", false))) {
                payment.tipOnCardReader();
            }
        }

        if (data.has("tip")){
            payment.tip(new BigDecimal(data.getString("tip")));
        }

        if (data.has("receiptEmail")) {
            String receiptEmail = data.getString("receiptEmail");
            payment.receiptEmail(receiptEmail);
        }

        if (data.has("receiptSMS")) {
            String receiptSMS = data.getString("receiptSMS");
            payment.receiptSMS(receiptSMS);
        }

        if (data.has("foreignTransactionId")) {
            String foreignTransactionId = data.getString("foreignTransactionId");
            if (foreignTransactionId != null) {
                payment.foreignTransactionId(foreignTransactionId);
            }
        }


        if (data.has("additionalInfo")) {
            JSObject additionalInfoLines = data.getJSObject("additionalInfo");
            if (additionalInfoLines != null) {
                for (Iterator<String> it = additionalInfoLines.keys(); it.hasNext(); ) {
                    String key = it.next();
                    payment.addAdditionalInfo(key, additionalInfoLines.getString(key));
                }
            }
        }

        if (data.has("configureRetryPolicy")) {
            JSObject retryPolicyData = data.getJSObject("configureRetryPolicy");
            RetryPolicy retryPolicy = new RetryPolicy(
                    retryPolicyData != null ? retryPolicyData.optLong("pollingInterval", 0L) : 0L,
                    retryPolicyData != null ? retryPolicyData.optLong("maxWaitingTime", 0L) : 0L,
                    retryPolicyData != null && retryPolicyData.optBoolean("disableBackButton", false)
            );
            payment.configureRetryPolicy(
                    retryPolicy.pollingInterval,
                    retryPolicy.maxWaitingTime,
                    retryPolicy.disableBackButton
            );
        }

        if (data.optBoolean("skipSuccessScreen", false)) {
            payment.skipSuccessScreen();
        }

        if (data.optBoolean("skipFailedScreen", false)) {
            payment.skipFailedScreen();
        }

        SumUpAPI.checkout(getActivity(), payment.build(), REQUEST_CODE_CHECKOUT);
    }

    @PluginMethod
    public void cardReaderPage(PluginCall call) {
        bridge.saveCall(call);
        SumUpAPI.openCardReaderPage(getActivity(), REQUEST_CODE_CARD_READER_PAGE);
    }

    @PluginMethod
    public void prepareCardTerminal(PluginCall call) {
        SumUpAPI.prepareForCheckout();
    }

    @PluginMethod
    public void logout(PluginCall call) {
        SumUpAPI.logout();
    }

    @ActivityCallback
    public void onActivityResult(PluginCall call, ActivityResult result) {
        if (result.getData() == null) {
            call.reject("Activity canceled or result data is null");
            return;
        }

        if (result.getData().getExtras() == null) {
            call.reject("Extras bundle is null");
            return;
        }

        int resultCode = result.getData().getExtras().getInt(SumUpAPI.Response.RESULT_CODE);
        String resultMessage = result.getData().getExtras().getString(SumUpAPI.Response.MESSAGE);

        if (resultCode == SumUpAPI.Response.ResultCode.SUCCESSFUL) {
            call.resolve(new JSObject().put("message", resultMessage).put("code", String.valueOf(resultCode)));
        } else {
            call.reject(resultMessage, String.valueOf(resultCode));
        }
    }

    private static class RetryPolicy {
        public long pollingInterval;
        public long maxWaitingTime;
        public boolean disableBackButton;

        public RetryPolicy(long pollingInterval, long maxWaitingTime, boolean disableBackButton) {
            this.pollingInterval = pollingInterval;
            this.maxWaitingTime = maxWaitingTime;
            this.disableBackButton = disableBackButton;
        }
    }
}