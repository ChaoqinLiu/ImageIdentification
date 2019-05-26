package com.example.sqlbrite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.sqlbrite.R;
import com.example.sqlbrite.app.BaseActivity;
import com.example.sqlbrite.fragment.DrivingLicenseHistoryFragment;
import com.example.sqlbrite.fragment.IDCardForBackHistoryFragment;
import com.example.sqlbrite.fragment.IDCardForFrontHistoryFragment;
import com.example.sqlbrite.fragment.ImageHistoryFragment;
import com.example.sqlbrite.fragment.TextHistoryFragment;
import com.example.sqlbrite.fragment.TranslationHistoryFragment;
import com.safframework.injectview.annotations.InjectView;

public class DisplayHistoryActivity extends BaseActivity {

    private final String type_image = "type_image";
    private final String type_text = "type_text";
    private final String type_id_card = "type_id_card";
    private final String type_bank_card = "type_bank_card";
    private final String type_license_plate = "type_license_plate";
    private final String type_driver_license = "type_driver_license";
    private final String type_train_ticket = "type_train_ticket";
    private final String type_passport = "type_passport";
    private final String type_driving_license = "type_driving_license";
    private final String type_business_license = "type_business_license";

    private String type;

    @InjectView(R.id.prompt)
    TextView prompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_display_history);

        type = getIntent().getStringExtra("type");
        displayHistoryByType();
    }

    private void displayHistoryByType(){

        switch (type) {
            case type_image:
                getDisplayImageHistoryFragment();
                break;
            case type_text:
                getDisplayTextHistoryFragment();
                break;
            case type_driving_license:
                getDisplayDrivingLicenseHistoryFragment();
                break;
            case type_id_card:
                getDisplayIDCardFrontHistoryFragment();
                break;
            default:
                break;
        }
    }

    private void getDisplayImageHistoryFragment() {
        ImageHistoryFragment fragment = new ImageHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
    }

    private void getDisplayDrivingLicenseHistoryFragment() {
        DrivingLicenseHistoryFragment fragment = new DrivingLicenseHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
    }

    private void getDisplayIDCardFrontHistoryFragment() {
        IDCardForFrontHistoryFragment fragment = new IDCardForFrontHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
    }

    private void getDisplayIDCardBackHistoryFragment() {
        IDCardForBackHistoryFragment fragment = new IDCardForBackHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
    }

    private void getTranslationHistoryFragment(){
        TranslationHistoryFragment fragment = new TranslationHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text, fragment).commit();
    }

    private void getDisplayTextHistoryFragment() {
        TextHistoryFragment fragment = new TextHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();

    }

    @Override
    public void onBackPressed(){
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_text);
        String current = currentFragment.toString().substring(0,currentFragment.toString().indexOf("{"));
        switch (current) {
            case "IDCardForFrontDetailsFragment":
                getDisplayIDCardFrontHistoryFragment();
                break;
            case "IDCardForFrontHistoryFragment":
                Intent();
                break;
            case "TextDetailsFragment":
                prompt.setVisibility(View.GONE);
                getDisplayTextHistoryFragment();
                break;
            case "TextHistoryFragment":
                Intent();
                break;
            case "TranslationDetailsFragment":

                getTranslationHistoryFragment();
                break;
            case "TranslationHistoryFragment":
                Intent();
                break;
            case "DrivingLicenseDetailsFragment":
                getDisplayDrivingLicenseHistoryFragment();
                break;
            case "DrivingLicenseHistoryFragment":
                Intent();
                break;
            case "IDCardForBackDetailsFragment":
                getDisplayIDCardBackHistoryFragment();
                break;
            case "IDCardForBackHistoryFragment":
                Intent();
                break;
        }
    }

    private void Intent(){
        Intent intent = new Intent(DisplayHistoryActivity.this, MainActivity.class);
        intent.putExtra("flag", "flag");
        startActivity(intent);
        finish();
    }

}
