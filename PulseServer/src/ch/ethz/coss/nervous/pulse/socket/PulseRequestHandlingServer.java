package ch.ethz.coss.nervous.pulse.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.java_websocket.WebSocket;

import ch.ethz.coss.nervous.pulse.PulseTimeMachineRequest;
import ch.ethz.coss.nervous.pulse.PulseWebSocketServer;
import ch.ethz.coss.nervous.pulse.utils.Log;

public class PulseRequestHandlingServer implements Runnable {

	private PulseWebSocketServer pWebSocketServer = null;
	private boolean stopped = false;
	private Thread runThread = null;
	private ExecutorService threadPool;
	private SqlFetchWorkerFactory factory;
	
	public  Hashtable<WebSocket, PulseTimeMachineRequest> hTimeMachineConnectionList = new Hashtable<WebSocket, PulseTimeMachineRequest>();
	private  ArrayList<PulseTimeMachineRequest> timeMachineRequestArrayList = new ArrayList<PulseTimeMachineRequest>();
	   

	public PulseRequestHandlingServer(int numThreads,
			SqlFetchWorkerFactory factory) {
//		this.pWebSocketServer = pWebSocketServer;
		this.threadPool = Executors.newFixedThreadPool(numThreads);
		this.factory = factory;
	}
	
	public void setPulseServer(PulseWebSocketServer pServer){
		this.pWebSocketServer = pServer;
	}

	
	
	public void addToRequestList(PulseTimeMachineRequest ptmr){
		timeMachineRequestArrayList.add(ptmr);
		
		System.out.println("added ptmr : \n");
		ptmr.print();
	}
	
	@Override
	public void run() {
		synchronized (this) {
			runThread = Thread.currentThread();
		}

		while (!isStopped()) {
			Iterator<PulseTimeMachineRequest> iterator = timeMachineRequestArrayList.iterator();
			while (iterator.hasNext()) {
				try {
					PulseTimeMachineRequest ptmRequest = (PulseTimeMachineRequest)iterator.next();
				    if(!ptmRequest.isNull) {
				    	hTimeMachineConnectionList.put(ptmRequest.webSocket, ptmRequest);
						this.threadPool.execute(factory.createWorker(
								pWebSocketServer, ptmRequest));
				    }
					
					
				} catch (Exception e) {
					e.printStackTrace();
					Log.getInstance().append(Log.FLAG_ERROR,
							"Threadpool execution failure");
				//TODO: check for 
//					java.util.ConcurrentModificationException
//					at java.util.ArrayList$Itr.checkForComodification(ArrayList.java:859)
//					at java.util.ArrayList$Itr.next(ArrayList.java:831)
//					at ch.ethz.coss.nervous.pulse.socket.PulseRequestHandlingServer.run(PulseRequestHandlingServer.java:60)
//					at java.lang.Thread.run(Thread.java:745)
					break;
				}
			}
			
			timeMachineRequestArrayList.clear();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				stopped = true;
				e.printStackTrace();
			}
			
		}
		threadPool.shutdown();
		Log.getInstance().append(Log.FLAG_INFO,
				"PulseRequestHandlingServer threading pool is shut down");
		
		
	    factory.createWorker(pWebSocketServer,factory.ptmRequest 
						);
	
		
	}

	private synchronized boolean isStopped() {
		return stopped;
	}

	public synchronized void stop() {
		threadPool.shutdown();
		
	}


}
