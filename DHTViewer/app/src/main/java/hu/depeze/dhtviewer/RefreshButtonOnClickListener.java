package hu.depeze.dhtviewer;

import android.content.Context;
import android.view.View;

/**
 * Created by depeze on 2016.01.10..
 */
public class RefreshButtonOnClickListener implements View.OnClickListener {

    private Context context = null;
    private HTTPGetAsyncResponse httpGetAsyncResponse = null;

    public RefreshButtonOnClickListener(Context context, HTTPGetAsyncResponse response) {
        this.context = context;
        this.httpGetAsyncResponse = response;
    }

    @Override
    public void onClick(View v) {
        DHTUtils.refresh(context, httpGetAsyncResponse);
    }

}
