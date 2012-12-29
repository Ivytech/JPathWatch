/*
 * JBelt
 * Copyright 2007-2012, ivytech srl
 *
 * Licensed under the GNU Lesser General Public License, version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

/**
 * @author tizianobellin
 *
 */

package org.jbelt.tools.client;

import java.io.IOException;
import java.util.List;

import name.pachler.nio.file.*;

public class jpathwatch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		WatchService watchService = FileSystems.getDefault().newWatchService();
		Path watchedPath = Paths.get("/Users/tizianobellin/Develop/tmp");
		
		WatchKey key = null;
		try {
		    key = watchedPath.register(watchService, StandardWatchEventKind.ENTRY_CREATE, StandardWatchEventKind.ENTRY_DELETE, StandardWatchEventKind.ENTRY_MODIFY);
		} catch (UnsupportedOperationException uox){
		    System.err.println("file watching not supported!");
		    // handle this error here
		}catch (IOException iox){
		    System.err.println("I/O errors");
		    // handle this error here
		}

		
		for(;;){
		    // take() will block until a file has been created/deleted
		    WatchKey signalledKey;
		    try {
		        signalledKey = watchService.take();
		    } catch (InterruptedException ix){
		        // we'll ignore being interrupted
		        continue;
		    } catch (ClosedWatchServiceException cwse){
		        // other thread closed watch service
		        System.out.println("watch service closed, terminating.");
		        break;
		    }

		    // get list of events from key
		    List<WatchEvent<?>> list = signalledKey.pollEvents();

		    // VERY IMPORTANT! call reset() AFTER pollEvents() to allow the
		    // key to be reported again by the watch service
		    signalledKey.reset();

		    // we'll simply print what has happened; real applications
		    // will do something more sensible here
		    for(WatchEvent e : list){
		        String message = "";
		        if(e.kind() == StandardWatchEventKind.ENTRY_CREATE){
		            Path context = (Path)e.context();
		            message = context.toString() + " created";
		        } else if(e.kind() == StandardWatchEventKind.ENTRY_DELETE){
		            Path context = (Path)e.context();
		            message = context.toString() + " deleted";
		        } else if(e.kind() == StandardWatchEventKind.ENTRY_MODIFY){
		            Path context = (Path)e.context();
		            message = context.toString() + " modified";
		        } else if(e.kind() == StandardWatchEventKind.OVERFLOW){
		            message = "OVERFLOW: more changes happened than we could retreive";
		        }
		        System.out.println(message);
		    }
		}		
	}

}
