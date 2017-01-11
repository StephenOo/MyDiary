package com.kiminonawa.mydiary.security;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kiminonawa.mydiary.R;
import com.kiminonawa.mydiary.shared.Encryption;
import com.kiminonawa.mydiary.shared.SPFManager;
import com.kiminonawa.mydiary.shared.statusbar.ChinaPhoneHelper;

/**
 * Created by daxia on 2017/1/10.
 */

public class PasswordActivity extends AppCompatActivity implements TextWatcher {


    /**
     * Mode
     */
    public static final int CREATE_PASSWORD = 0;
    public static final int CREATE_PASSWORD_WITH_VERIFY = 1;
    public static final int VERIFY_PASSWORD = 2;
    public static final int REMOVE_PASSWORD = 3;

    private int currentMode = CREATE_PASSWORD;
    private String password;
    //For verify password;
    private String createdPassword;

    private boolean LockForTextWatcher = false;

    /**
     * UI
     */
    private EditText EDT_password_number_1, EDT_password_number_2, EDT_password_number_3,
            EDT_password_number_4;

    private Button But_password_key_1, But_password_key_2, But_password_key_3,
            But_password_key_4, But_password_key_5, But_password_key_6,
            But_password_key_7, But_password_key_8, But_password_key_9,
            But_password_key_cancel, But_password_key_0;
    private ImageButton But_password_key_backspace;


    private TextView TV_password_message;


    //start lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        //For set status bar
        ChinaPhoneHelper.setStatusBar(this, true);

        EDT_password_number_1 = (EditText) findViewById(R.id.EDT_password_number_1);
        EDT_password_number_2 = (EditText) findViewById(R.id.EDT_password_number_2);
        EDT_password_number_3 = (EditText) findViewById(R.id.EDT_password_number_3);
        EDT_password_number_4 = (EditText) findViewById(R.id.EDT_password_number_4);

        EDT_password_number_1.addTextChangedListener(this);
        EDT_password_number_2.addTextChangedListener(this);
        EDT_password_number_3.addTextChangedListener(this);
        EDT_password_number_4.addTextChangedListener(this);

        TV_password_message = (TextView) findViewById(R.id.TV_password_message);

        But_password_key_1 = (Button) findViewById(R.id.But_password_key_1);
        But_password_key_2 = (Button) findViewById(R.id.But_password_key_2);
        But_password_key_3 = (Button) findViewById(R.id.But_password_key_3);
        But_password_key_4 = (Button) findViewById(R.id.But_password_key_4);
        But_password_key_5 = (Button) findViewById(R.id.But_password_key_5);
        But_password_key_6 = (Button) findViewById(R.id.But_password_key_6);
        But_password_key_7 = (Button) findViewById(R.id.But_password_key_7);
        But_password_key_8 = (Button) findViewById(R.id.But_password_key_8);
        But_password_key_9 = (Button) findViewById(R.id.But_password_key_9);
        But_password_key_cancel = (Button) findViewById(R.id.But_password_key_cancel);
        But_password_key_0 = (Button) findViewById(R.id.But_password_key_0);
        But_password_key_backspace = (ImageButton) findViewById(R.id.But_password_key_backspace);

        initUI();
    }

    private void initUI() {
        switch (currentMode) {
            case CREATE_PASSWORD:
                TV_password_message.setText("請輸入新密碼");
                break;
            case CREATE_PASSWORD_WITH_VERIFY:
                TV_password_message.setText("請再輸一次密碼");
                break;
            case VERIFY_PASSWORD:
                TV_password_message.setText("請輸入密碼");
                break;
            case REMOVE_PASSWORD:
                TV_password_message.setText("請輸入舊密碼");
                break;
        }
    }

    private void clearUiPassword() {
        EDT_password_number_1.setText("");
        EDT_password_number_2.setText("");
        EDT_password_number_3.setText("");
        EDT_password_number_4.setText("");
    }

    private void afterPasswordChanged() {
        Log.e("test", "afterPasswordChanged");
        switch (currentMode) {
            case CREATE_PASSWORD:
                createdPassword = getPasswordFromUI();
                clearUiPassword();
                currentMode = CREATE_PASSWORD_WITH_VERIFY;
                initUI();
                break;
            case CREATE_PASSWORD_WITH_VERIFY:
                password = getPasswordFromUI();
                if (createdPassword.equals(password)) {
                    savePassword(password);
                    finish();
                } else {
                    clearUiPassword();
                    Toast.makeText(this, "你輸入的密碼有誤", Toast.LENGTH_LONG).show();
                }
                break;
            case VERIFY_PASSWORD:
                password = getPasswordFromUI();
                if (isPasswordCorrect(password)) {
                    clearUiPassword();
                } else {
                    clearUiPassword();
                    Toast.makeText(this, "你輸入的密碼有誤", Toast.LENGTH_LONG).show();
                }
                break;
            case REMOVE_PASSWORD:
                if (isPasswordCorrect(password)) {
                    clearUiPassword();
                    savePassword("");
                } else {
                    clearUiPassword();
                    Toast.makeText(this, "你輸入的密碼有誤", Toast.LENGTH_LONG).show();
                }
                break;
        }
        LockForTextWatcher = false;
    }

    private String getPasswordFromUI() {
        StringBuilder passwordStringBuilder = new StringBuilder();
        passwordStringBuilder.append(EDT_password_number_1.getText().toString());
        passwordStringBuilder.append(EDT_password_number_2.getText().toString());
        passwordStringBuilder.append(EDT_password_number_3.getText().toString());
        passwordStringBuilder.append(EDT_password_number_4.getText().toString());
        return passwordStringBuilder.toString();
    }

    private boolean isPasswordCorrect(String password) {
        return Encryption.SHA256(SPFManager.getPassword(this)).equals(password);
    }

    private void savePassword(String password) {
        SPFManager.setAndEncryptPassword(this, password);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!LockForTextWatcher) {
            Log.e("test", "afterTextChanged");
            if (EDT_password_number_1.getText().length() == 1) {
                EDT_password_number_1.clearFocus();
                EDT_password_number_2.requestFocus();
            }

            if (EDT_password_number_2.getText().length() == 1) {
                EDT_password_number_2.clearFocus();
                EDT_password_number_3.requestFocus();
            }

            if (EDT_password_number_3.getText().length() == 1) {
                EDT_password_number_3.clearFocus();
                EDT_password_number_4.requestFocus();
            }

            if (EDT_password_number_4.getText().length() == 1) {
                EDT_password_number_4.clearFocus();
                LockForTextWatcher = true;
                afterPasswordChanged();
            }
        }
    }
}
