package com.hui.tallybook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hui.tallybook.db.DBManager;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void onClick(View view) {
        if(view.getId() == R.id.setting_iv_back){
            finish();
        }
        else if(view.getId() == R.id.setting_tv_clear){
            showDeleteDialog();
        }
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("清除数据")
               .setMessage("确定要清除所有数据吗？\n删除后无法恢复，请慎重选择")
               .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager.deleteAllAccounttb();
                        Toast.makeText(SettingActivity.this, "清除成功", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }
}