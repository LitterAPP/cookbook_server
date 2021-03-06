package plugs;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jws.Logger;
import task.CheckTogetherValid;
import task.FrogRank;

public class Init implements jws.Init{

	private static ScheduledExecutorService service = Executors.newScheduledThreadPool(10); 
	
	@Override
	public void init() {
		Logger.info("Plugs init...");
		//service.scheduleAtFixedRate(new CheckTogetherValid(), 0, 10, TimeUnit.MINUTES); 
		service.scheduleAtFixedRate(new FrogRank(), 0, 10, TimeUnit.MINUTES);  
 	}

}
