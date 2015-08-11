import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxEvent;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;
import com.box.sdk.BoxUser;
import com.box.sdk.EventListener;
import com.box.sdk.EventStream;
import com.box.sdk.EventLog;

public final class viewStats {
    private static final String DEVELOPER_TOKEN = "TymWz65FbQB1bBjd1lG1w3v7xz7MGmZM";
    private static final int MAX_DEPTH = 1;

    private viewStats() { }

    public static void main(String[] args) {
        // Turn off logging to prevent polluting the output.
        Logger.getLogger("com.box.sdk").setLevel(Level.OFF);

        BoxAPIConnection api = new BoxAPIConnection(DEVELOPER_TOKEN);

        BoxUser.Info userInfo = BoxUser.getCurrentUser(api).getInfo();
        System.out.format("Welcome, %s <%s>!\n\n", userInfo.getName(), userInfo.getLogin());

        BoxFolder rootFolder = BoxFolder.getRootFolder(api);
        eventListener(api);
        //listFolder(rootFolder, 0);
        //eventLog(api);
    }

    private static void listFolder(BoxFolder folder, int depth) {
        for (BoxItem.Info itemInfo : folder) {
            String indent = "";
            for (int i = 0; i < depth; i++) {
                indent += "    ";
            }
            System.out.println(indent + itemInfo.getName());
            System.out.println(itemInfo.getSharedLink());//.getDownloadCount());
            if (itemInfo instanceof BoxFolder.Info) {
                BoxFolder childFolder = (BoxFolder) itemInfo.getResource();
                if (depth < MAX_DEPTH) {
                    listFolder(childFolder, depth + 1);
                }
            }
        }
    }
    
    private static void eventListener(BoxAPIConnection api){
    	EventStream stream = new EventStream(api);
    	stream.addListener(new EventListener() {
    	    public void onEvent(BoxEvent event) {
    	        // Handle the event.
    	    	System.out.println(event);
    	    }
			@Override
			public boolean onException(Throwable arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			@Override
			public void onNextPosition(long arg0) {
				// TODO Auto-generated method stub
				System.out.println(arg0);
			}
    	});
    	stream.start();
    }
    
    private static void eventLog(BoxAPIConnection api){
        SimpleDateFormat dateformat3 = new SimpleDateFormat("dd/MM/yyyy");
        try {
			Date after = dateformat3.parse("01/08/2015");
			Date before = dateformat3.parse("10/08/2015");
	    	EventLog log = EventLog.getEnterpriseEvents(api, after, before);
	    	System.out.println(log);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
