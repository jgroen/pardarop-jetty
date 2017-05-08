package com.wiscoproj.testP2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;


public class MobServer 
{
	
	private static String BASEDIR = "files/opt3/equitorial/";
	private static ServerSocket myServerSock = null;
	private static Socket clientConn = null;
	private static OutputStream outSend = null;
	private static MessageUnpacker unpacker = null;
	private static int whichPort=8088;
	
    public static void main( String[] args ) 
    		throws IOException
    {
    	int tPort = 0;
    	if (args.length>0){
    		BASEDIR = args[0];
    		try {
    			tPort = Integer.parseInt(args[1]);
    		}
    		catch (Exception e){
    			System.out.println("parse port: "+e.getMessage());
    		}
    	}
       if (tPort > 0) {
    	   whichPort = tPort;
       }
        
        System.out.println("starting server path (1): "+BASEDIR+"    port (2):"+Integer.toString(whichPort));
        
        try {
	            myServerSock = new ServerSocket(whichPort);

        } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port "
                    + whichPort + " or listening for a connection");
                System.out.println(e.getMessage());
        }
   
        
        long firstStartTime=System.currentTimeMillis();
        int numRuns=10;

        while (numRuns>0) {
        	numRuns--;
	        clientConn=myServerSock.accept();
			outSend = clientConn.getOutputStream();
			unpacker = MessagePack.newDefaultUnpacker(clientConn.getInputStream());
	
	        
			MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
			long startTime = System.currentTimeMillis();
			boolean done = false;
			while (!done && (System.currentTimeMillis()-startTime)<300000){
				//System.out.println("unpack hasNext loop: "+(System.currentTimeMillis()-startTime));
				if (unpacker.hasNext()) {
					System.out.println("unpack hasNext inner loop: "+(System.currentTimeMillis()-startTime));
					String curMode = unpacker.unpackString();
					startTime = System.currentTimeMillis(); //reset on hasNext
					switch (curMode) {
			        		case "GETFILES":
			        			List<String> getFileList = new ArrayList<String>();
			        			int numFiles = 0;
								if (unpacker.hasNext()) {
									numFiles = unpacker.unpackArrayHeader();
									String oneFile="";
									for (int i=1;i<=numFiles;i++){
										oneFile=unpacker.unpackString();
										getFileList.add(oneFile);
										System.out.println(oneFile);
									}
								}
			        			//for (String oneFile : getFileList) {
								for (int i=0;i<numFiles;i++) {
									String oneFile = getFileList.get(i);
			        		        String filePath = BASEDIR + oneFile;
			        		        if (Files.exists(Paths.get(filePath))) {
				        		        byte[] outBytes = Files.readAllBytes(Paths.get(filePath));
				        		        packer.packString(oneFile);
				        		        packer.packString("OK");
				        		        packer.packBinaryHeader(outBytes.length);
				        		        packer.addPayload(outBytes);
			        		        }
			        		        else {
			        		        	packer.packString(oneFile);
			        		        	packer.packString("NOTFOUND");
			        		        }
								}
								
								packer.close();
				        		outSend.write(packer.toByteArray());
				        		packer.clear();
				        		
			        			break;
			        		case "LISTFILES":
			        			List<String> fileList = new ArrayList<String>();
			        			
			        			try(Stream<Path> paths = Files.walk(Paths.get(BASEDIR))) {
			        			    paths.forEach(filePath -> {
			        			        if (Files.isRegularFile(filePath)) {
			        			            System.out.println(filePath);
			        			            fileList.add(filePath.getFileName().toString());
			        			        }
			        			    });
			        			}
			        			
			        			packer.packArrayHeader(fileList.size());
			        			for (String oneFile : fileList){
			        				packer.packString(oneFile);
			        			}
			        			
			    	        	
				         		packer.close();
				        		outSend.write(packer.toByteArray());
				        		packer.clear();
				        		
			        			break;
			        			
			        		case "CLOSE":
			        			System.out.println("Close received");
			        			done=true;
			        			
			        			packer.packString("OKCLOSE");
			        			packer.close();
				        		outSend.write(packer.toByteArray());
				        		packer.clear();
			        			
			        			//clientConn.close();
			        			//clientConn=null;
			        			break;
		        		
		        		}
	
					}
				}
				if (clientConn!=null) {
					System.out.println("Closing channel");
					clientConn.close();
					clientConn=null;
				}
        }
        
        myServerSock.close();
        System.out.println("exiting");
        
        
    }
}
