package pro.sonali.com.newsoap;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class SoapActivity extends Activity {

    private static final String SOAP_ACTION = "http://tempuri.org/IService/GetFlight";
    private static final String METHOD_NAME = "GetFlight";
    private static final String NAMESPACE = "http://tempuri.org/";
    private final String URL = "http://csc523-001-site1.atempurl.com/Service.svc/soapService";

    ProgressDialog progressDialog;

    Button b; // Action
    TextView tv; // Result
    EditText et; // Flight_ID

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soap);
        //Edit Control
        et = (EditText) findViewById(R.id.editText1);
        //Text control
        tv = (TextView) findViewById(R.id.tv_result); //Assigne Filght Status
        //Button to trigger web service invocation
        b = (Button) findViewById(R.id.button1);
        //Button Click Listener
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                progressDialog = new ProgressDialog(SoapActivity.this);
                progressDialog.setMessage("Searching ...");
                progressDialog.show();

             new CallWebService().execute(et.getText().toString()); // ... GetFlight(et) .. et

            }
        });
    }



    class CallWebService extends AsyncTask<String, Void, String> {
       String edit = et.getText().toString(); //Get F_ID

        @Override
        protected void onPostExecute(String s) {
            tv.setText("  " + s);
            progressDialog.cancel();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            String ss = "";
            SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_NAME);
            soapObject.addProperty("Id_flight", edit);

            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11); //VER11: SOAP Version 1.1 __ constant webBinding
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(soapObject);
            envelope.dotNet = true;

            HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
            httpTransportSE.debug = true;

            try {
                httpTransportSE.call(SOAP_ACTION, envelope);

                result = String.valueOf((Object )envelope.getResponse());
                SoapObject resultsoap=(SoapObject)envelope.getResponse();

               //To get the data.
                 ss = resultsoap.getProperty(0).toString() ;

                System.out.println(" " +soapObject+ "  "+result+"  "+ss+"  ");

            } catch (Exception e) {
                System.out.println("Error Msg. : "+e.getMessage());
                e.printStackTrace();
            }
           // return result;
            return ss;
        }
    }



}
