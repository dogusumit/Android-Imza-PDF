package com.example.pdfciiii;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

public class MainActivity extends Activity {
	
	RelativeLayout rltv;
	String yol;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


	    rltv = (RelativeLayout) findViewById(R.id.relativelayout);
		Button btn1= (Button) findViewById(R.id.button1);
		Button btn2= (Button) findViewById(R.id.buton2);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				File asd = new File(Environment.getExternalStorageDirectory(),"Seyhan Belediyesi");
		    	 if (!asd.exists()) {
		    		    asd.mkdir();
		    		}
		    	  yol= asd.getPath()+"/"+ new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().getTime())+".pdf";
					if(createPdf(rltv,yol))
						Toast.makeText(getApplicationContext(), yol+"\n Kaydedildi!", Toast.LENGTH_LONG).show();
				}
		});
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try{
					BluetoothAdapter btadpt=BluetoothAdapter.getDefaultAdapter();
					if(btadpt==null)
					{
						Toast.makeText(getApplicationContext(), "Cihazınızda Bluetooth Özelliği Mevcut Değil!", Toast.LENGTH_LONG).show();
					}
					else
					{
						if (!btadpt.isEnabled()) {
							Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
							startActivityForResult(intent, 1000);
						} 
				    String className = null;
				    String packageName = null;

				    File file = new File(yol);

				    Intent intent = new Intent();
				    intent.setAction(Intent.ACTION_SEND);
				    intent.setType("text/plain");
				    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file) );

				    PackageManager pm = getPackageManager();
				    List<ResolveInfo> appsList = pm.queryIntentActivities( intent, 0);

				    if(appsList.size() > 0 ){
				        for(ResolveInfo info: appsList){
				            packageName = info.activityInfo.packageName;
				            if( packageName.equals("com.android.bluetooth")){
				                className = info.activityInfo.name;
				                break;
				            }
				        }
				    }
				    intent.setClassName(packageName, className);
				    startActivity(intent);
				}
				}
				catch(Exception e)
				{
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
	
	}

	private boolean createPdf(View view,String pdf){
    	try{
	         Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
	         Canvas canvas = new Canvas(returnedBitmap);
	         Drawable bgDrawable =view.getBackground();
	         if (bgDrawable!=null) 
	             bgDrawable.draw(canvas);
	         else 
	             canvas.drawColor(Color.WHITE);
	         view.draw(canvas);
        	Document document=new Document();
        	PdfWriter.getInstance(document,new FileOutputStream(pdf));
	        ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        returnedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
	        byte[] byteArray = stream.toByteArray();
	        Image img=Image.getInstance(byteArray);
	        img.setAbsolutePosition(0, 0);
	        document.open();
	        document.setPageSize(img);
	        document.newPage();
	        document.add(img);        
	        document.close();
	        return true;
    	}
    	catch(Exception e)
    	{
    		Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
    		return false;
    	}
    	
	}
	
}
