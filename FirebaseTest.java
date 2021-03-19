package firebasetest;

import java.io.FileInputStream;

import javax.swing.JFrame;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FirebaseTest {

	public static void main(String[] args) {

		// Firebase を使用するための初期設定。

		try {
			
			String json_path = "C:\\Users\\Eiichi\\Desktop\\MyApplication\\fir-test-e9a48-firebase-adminsdk-zwdbd-dd85830491.json"; 

			FileInputStream serviceAccount =
			  new FileInputStream(json_path);

			FirebaseOptions options = new FirebaseOptions.Builder()
			  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
			  .setDatabaseUrl("https://fir-test-e9a48-default-rtdb.firebaseio.com")
			  .build();

			FirebaseApp.initializeApp(options);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Swing の JFrame を画面に表示。

		FirebaseFrame frame = new FirebaseFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	}
}
