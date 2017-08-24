package com.xuhong.smarthome.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.gyf.barlibrary.ImmersionBar;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.bean.CollectionUrl;
import com.xuhong.smarthome.bean.User;
import com.xuhong.smarthome.utils.L;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyCollectionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImmersionBar.setTitleBar(this, toolbar);

        String objectId = User.getCurrentUser().getObjectId();
        BmobQuery<CollectionUrl> query = new BmobQuery<>();
        //用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
        User user = new User();
        user.setObjectId(objectId);
        query.addWhereEqualTo("user", new BmobPointer(user));
        query.findObjects(new FindListener<CollectionUrl>() {
            @Override
            public void done(List<CollectionUrl> objects, BmobException e) {
                for (CollectionUrl c :objects) {
                    L.e("收藏的地址："+c.getUri());
                }
            }
        });
    }
}
