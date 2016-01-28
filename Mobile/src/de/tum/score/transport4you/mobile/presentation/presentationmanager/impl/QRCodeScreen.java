package de.tum.score.transport4you.mobile.presentation.presentationmanager.impl;

//import com.example.qrcodetest.Contents;
//import com.example.qrcodetest.QRCodeEncoder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import de.tum.score.transport4you.mobile.R;
import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;
import de.tum.score.transport4you.mobile.application.applicationcontroller.impl.ApplicationSingleton;
import de.tum.score.transport4you.mobile.presentation.presentationmanager.IPresentation;
import de.tum.score.transport4you.mobile.presentation.qrcode.Contents;
import de.tum.score.transport4you.mobile.presentation.qrcode.QRCodeEncoder;

public class QRCodeScreen extends Activity implements IPresentation{
	private IMainApplication mainApplication;
	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.qrcode);
	        
	    mainApplication = ApplicationSingleton.getApplicationController();
	    mainApplication.registerActivity(this);
	    
	    Intent intent = getIntent();
	    String qrInputText = intent.getStringExtra("ETicket");
	    String Enc = intent.getStringExtra("Enc");
	    TextView textView = (TextView) findViewById(R.id.qrcode_description);
	    //textView.setText(qrInputText);
	    textView.setText("ETicket QR Code");
	    
	    //Find screen size
	    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
		   Display display = manager.getDefaultDisplay();
		   Point point = new Point();
		   display.getSize(point);
		   int width = point.x;
		   int height = point.y;
		   int smallerDimension = width < height ? width : height;
		   smallerDimension = smallerDimension * 3/4;

		   //Encode with a QR Code image
		   QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(Enc, 
		             null, 
		             Contents.Type.TEXT,  
		             BarcodeFormat.QR_CODE.toString(), 
		             smallerDimension);
		   try {
		    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
		    ImageView myImage = (ImageView) findViewById(R.id.imageViewQRCode);
		    myImage.setImageBitmap(bitmap);

		   } catch (WriterException e) {
		    e.printStackTrace();
		   }
	 }
	
	public void onDestroy() {
		super.onDestroy();
	}	
	
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		this.finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.mn_exit:
	        mainApplication.shutdownSystem();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	@Override
	public void updateProgessDialog(String title, String message, boolean visible, Integer increment) {
		// TODO Auto-generated method stub
		
	}

}
