package phong.android.com.utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONObject;

import phong.android.com.entity.MessageWithIP;
import phong.android.com.entity.Recent;
import phong.android.com.entity.UserLogin;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

public class DataSingleton {
	private static Socket socket;
	private static ObjectInputStream ois;
	private static ObjectOutputStream oos;

	// day la arrr static
	private static ArrayList<UserLogin> arrUserLogin = new ArrayList<UserLogin>();
	private static ArrayList<Recent> arrRecent = new ArrayList<Recent>();
	private static ArrayList<UserLogin> arrFavorite = new ArrayList<UserLogin>();
	private static UserLogin me = null;

	public static synchronized Socket getSocket() {
		return socket;
	}

	public static synchronized void setSocket(Socket socket) {
		DataSingleton.socket = socket;
	}

	public static synchronized ObjectInputStream getOis() {
		return ois;
	}

	public static synchronized void setOis(ObjectInputStream ois) {
		DataSingleton.ois = ois;
	}

	public static synchronized ObjectOutputStream getOos() {
		return oos;
	}

	public static synchronized void setOos(ObjectOutputStream oos) {
		DataSingleton.oos = oos;
	}

	public static ArrayList<UserLogin> getArrUserLogin() {
		return arrUserLogin;
	}

	public static void setArrUserLogin(ArrayList<UserLogin> arrUserLogin) {
		DataSingleton.arrUserLogin = arrUserLogin;
	}

	public static ArrayList<Recent> getArrRecent() {
		return arrRecent;
	}

	public static void setArrRecent(ArrayList<Recent> arrRecent) {
		DataSingleton.arrRecent = arrRecent;
	}

	public static ArrayList<UserLogin> getArrFavorite() {
		return arrFavorite;
	}

	public static void setArrFavorite(ArrayList<UserLogin> arrFavorite) {
		DataSingleton.arrFavorite = arrFavorite;
	}

	public static synchronized UserLogin getMe() {
		return me;
	}

	public static synchronized void setMe(UserLogin me) {
		DataSingleton.me = me;
	}

	// add them du lieu vao mang 
	public static synchronized void addFavorite(UserLogin userLogin) {
		DataSingleton.arrFavorite.add(userLogin);
	}
	public static synchronized void addPeople(UserLogin userLogin) {
		DataSingleton.arrUserLogin.add(userLogin);
	}
	
//	public static synchronized int getIndex(String userLogin){
//		
//	for(UserLogin u : DataSingleton.arrFavorite){
//		if(u.getUsersName().endsWith(userLogin)){
//			return u;
//			
//		}
//	}
//	}
	
	public static synchronized void deleteFavorite(UserLogin userLogin){
		DataSingleton.arrFavorite.remove(0);
	}

	public static synchronized UserLogin getUser(String username) {
		for (UserLogin u : DataSingleton.arrUserLogin) {
			if (u.getUsersName().equals(username))
				return u;
		}
		return null;
	}

	public static synchronized void sendToServer(String obj) {
		try {

			oos.writeObject(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized void addMessage(UserLogin userLogin,
			MessageWithIP msg) {
		if (arrRecent.size() == 0) {
			ArrayList<MessageWithIP> arr = new ArrayList<MessageWithIP>();
			arr.add(msg);
			Recent rc = new Recent(userLogin, arr);
			DataSingleton.arrRecent.add(0, rc);
			return;

		}
		for (int i = 0; i < arrRecent.size(); i++) {
			Recent r = arrRecent.get(i);
			if (r.getUserLogin().getUsersName()
					.equals(userLogin.getUsersName())) {
				ArrayList<MessageWithIP> tmp = r.getRecentMessage();
				tmp.add(msg);
				Recent tmp1 = new Recent(userLogin, tmp);
				DataSingleton.arrRecent.set(i, tmp1);
				return;
			}
		}
		ArrayList<MessageWithIP> arr = new ArrayList<MessageWithIP>();
		arr.add(msg);
		Recent rc = new Recent(userLogin, arr);
		DataSingleton.arrRecent.add(rc);
	}

	public static synchronized ArrayList<MessageWithIP> getMessageList(
			UserLogin usersLogin) {
		ArrayList<MessageWithIP> arr = new ArrayList<MessageWithIP>();
		for (Recent r : DataSingleton.getArrRecent()) {
			UserLogin u = r.getUserLogin();
			if (u.getUsersName().equals(usersLogin.getUsersName())) {
				arr = r.getRecentMessage();
			}
		}
		return arr;
	}

	public static synchronized JSONObject receive() {
		JSONObject obj = null;
		try {
			String a = String.valueOf(ois.readObject());
			obj = new JSONObject(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	// public static synchronized void changeStatus(String newStt) {
	//
	// UserLogin p = new UserLogin(me.getUsersName(), null, me.getUsersName(),
	// newStt);
	// DataHandler.me = p;
	// }

	public static synchronized void saveImage(String username, String filename,
			String bitmapStr) {
		try {
			Bitmap bitmap = stringToBitMap(bitmapStr);
			filename = filename.replace(" ", "");
			filename = filename.replace("/", "");
			filename = filename.replace(",", "");
			filename = filename.replace(":", "");
			if (android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				File sdCard = Environment.getExternalStorageDirectory();
				File dir = new File(sdCard.getAbsolutePath() + "/message/"
						+ username);
				dir.mkdirs();
				File file = new File(dir, filename + ".png");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
				FileOutputStream f = null;
				f = new FileOutputStream(file);
				if (f != null) {
					f.write(baos.toByteArray());
					f.flush();
					f.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String bitMapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		String temp = Base64.encodeToString(b, Base64.DEFAULT);
		return temp;
	}

	public static Bitmap stringToBitMap(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
					encodeByte.length);
			return bitmap;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}

}
