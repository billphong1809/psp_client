//package phong.android.com.adapter;
//
//import java.util.List;
//
//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.NetworkImageView;
//
//import phong.android.com.R;
//import phong.android.com.entity.Message;
//import phong.android.com.utilities.AppController;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//public class AdapterMessageList extends BaseAdapter {
//
//	private Context context;
//	private List<Message> messagesItems;
//	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
//
//	public AdapterMessageList(Context context,
//			List<Message> navDrawerItems) {
//		this.context = context;
//		this.messagesItems = navDrawerItems;
//	}
//
//	@Override
//	public int getCount() {
//		return messagesItems.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return messagesItems.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@SuppressLint("InflateParams")
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//
//		Message m = messagesItems.get(position);
//
//		LayoutInflater mInflater = (LayoutInflater) context
//				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//
////		if (messagesItems.get(position).isSelf()) {
//
//			convertView = mInflater.inflate(R.layout.list_item_message_right,
//					null);
//		} else {
//
//			convertView = mInflater.inflate(R.layout.list_item_message_left,
//					null);
//		}
//
//		NetworkImageView nwImageView = (NetworkImageView) convertView
//				.findViewById(R.id.thumnaidAvatar);
//		TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);
//
//		txtMsg.setText(m.getMessage());
//		nwImageView.setImageUrl(m.getThumnaidFromName(), imageLoader);
//
//		return convertView;
//	}
//
//}
