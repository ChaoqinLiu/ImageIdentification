package com.example.sqlbrite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;

import com.example.sqlbrite.R;
import com.example.sqlbrite.app.BaseActivity;
import com.example.sqlbrite.fragment.BankCardHistoryFragment;
import com.example.sqlbrite.fragment.BusinessLicenseHistoryFragment;
import com.example.sqlbrite.fragment.DriverLicenseHistoryFragment;
import com.example.sqlbrite.fragment.DrivingLicenseHistoryFragment;
import com.example.sqlbrite.fragment.IDCardForBackHistoryFragment;
import com.example.sqlbrite.fragment.IDCardForFrontHistoryFragment;
import com.example.sqlbrite.fragment.ImageHistoryFragment;
import com.example.sqlbrite.fragment.LicensePlateHistoryFragment;
import com.example.sqlbrite.fragment.PassportHistoryFragment;
import com.example.sqlbrite.fragment.TextHistoryFragment;
import com.example.sqlbrite.fragment.TrainTicketHistoryFragment;
import com.example.sqlbrite.fragment.TranslationHistoryFragment;

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
                getImageHistoryFragment();
                break;
            case type_text:
                getTextHistoryFragment();
                break;
            case type_driving_license:
                getDrivingLicenseHistoryFragment();
                break;
            case type_id_card:
                getIDCardFrontHistoryFragment();
                break;
            case type_business_license:
                getBusinessLicenseHistoryFragment();
                break;
            case type_bank_card:
                getBankCardHistoryFragment();
                break;
            case type_driver_license:
                getDriverLicenseHistoryFragment();
                break;
            case type_license_plate:
                getLicensePlateHistoryFragment();
                break;
            case type_passport:
                getPassportHistoryFragment();
                break;
            case type_train_ticket:
                getTrainTicketHistoryFragment();
                break;
            default:
                break;
        }
    }

    private void getImageHistoryFragment() {
        ImageHistoryFragment fragment = new ImageHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
    }

    private void getDrivingLicenseHistoryFragment() {
        DrivingLicenseHistoryFragment fragment = new DrivingLicenseHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
    }

    private void getIDCardFrontHistoryFragment() {
        IDCardForFrontHistoryFragment fragment = new IDCardForFrontHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
    }

    private void getIDCardBackHistoryFragment() {
        IDCardForBackHistoryFragment fragment = new IDCardForBackHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
    }

    private void getTranslationHistoryFragment(){
        TranslationHistoryFragment fragment = new TranslationHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text, fragment).commit();
    }

    private void getTextHistoryFragment() {
        TextHistoryFragment fragment = new TextHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();

    }

    private void getBusinessLicenseHistoryFragment() {
        BusinessLicenseHistoryFragment fragment = new BusinessLicenseHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
    }

    private void getBankCardHistoryFragment() {
        BankCardHistoryFragment fragment = new BankCardHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
    }

    private void getDriverLicenseHistoryFragment() {
        DriverLicenseHistoryFragment fragment = new DriverLicenseHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
    }

    private void getLicensePlateHistoryFragment() {
        LicensePlateHistoryFragment fragment = new LicensePlateHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
    }

    private void getPassportHistoryFragment() {
        PassportHistoryFragment fragment = new PassportHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
    }

    private void getTrainTicketHistoryFragment() {
        TrainTicketHistoryFragment fragment = new TrainTicketHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
    }

    private void Intent(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("flag", "flag");
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_text);
        String current = currentFragment.toString().substring(0,currentFragment.toString().indexOf("{"));
        switch (current) {
            case "IDCardForFrontDetailsFragment":
                getIDCardFrontHistoryFragment();
                break;
            case "TextDetailsFragment":
                getTextHistoryFragment();
                break;
            case "TranslationDetailsFragment":
                getTranslationHistoryFragment();
                break;
            case "DrivingLicenseDetailsFragment":
                getDrivingLicenseHistoryFragment();
                break;
            case "IDCardForBackDetailsFragment":
                getIDCardBackHistoryFragment();
                break;
            case "BusinessLicenseDetailsFragment":
                getBusinessLicenseHistoryFragment();
                break;
            case "BankCardDetailsFragment":
                getBankCardHistoryFragment();
                break;
            case "DriverLicenseDetailsFragment":
                getDriverLicenseHistoryFragment();
                break;
            case "LicensePlateDetailsFragment":
                getLicensePlateHistoryFragment();
                break;
            case "PassportDetailsFragment":
                getPassportHistoryFragment();
                break;
            case "TrainTicketDetailsFragment":
                getTrainTicketHistoryFragment();
                break;
            default:
                Intent();
                break;
        }
    }

}
