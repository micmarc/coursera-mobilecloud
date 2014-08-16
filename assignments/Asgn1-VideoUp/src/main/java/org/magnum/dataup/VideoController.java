/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.magnum.dataup;

import static org.magnum.dataup.VideoSvcApi.VIDEO_DATA_PATH;
import static org.magnum.dataup.VideoSvcApi.VIDEO_SVC_PATH;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import retrofit.client.Response;

@Controller
public class VideoController {
	
	private static final AtomicLong currentId = new AtomicLong(0L);
	
	private final Map<Long, Video> videos = new ConcurrentHashMap<Long, Video>();
	private final VideoFileManager videoDataMgr;
	
	public VideoController() throws IOException {
		videoDataMgr = VideoFileManager.get();
	}
	
	@RequestMapping(value=VIDEO_SVC_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Video> getVideos() {
		return videos.values();
	}

	@RequestMapping(value=VIDEO_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody Video addVideo(@RequestBody Video v) {
		// Generate and set unique ID
		long id = generateAndSetId(v);
		
		// Generate data URL
		generateAndSetDataUrl(v);
		
		// Add to our in-memory video list
		videos.putIfAbsent(id, v);
		
		return v;
	}

	@RequestMapping(value=VIDEO_DATA_PATH, method=RequestMethod.POST)
	public @ResponseBody VideoStatus setVideoData(@PathVariable("id") long id, @RequestParam MultipartFile videoData) {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value=VIDEO_DATA_PATH, method=RequestMethod.GET)
	public Response getData(@PathVariable("id") long id) {
		// TODO Auto-generated method stub
		return null;
	}

	private void generateAndSetDataUrl(final Video v) {
		v.setDataUrl(getDataUrl(v.getId()));
	}

	private long generateAndSetId(final Video v) {
		long id = v.getId();
		if (id == 0) { 
			// ID is not set, so generate one.
			id = currentId.incrementAndGet();
			v.setId(id);
		}
		return id;
	}
	
	private String getDataUrl(long videoId){
        String url = getUrlBaseForLocalServer() + "/video/" + videoId + "/data";
        return url;
    }

	private String getUrlBaseForLocalServer() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		String base = "http://" + request.getServerName()
				+ ((request.getServerPort() != 80) ? ":"+ request.getServerPort() : "");
		return base;
	}

	private void saveVideo(Video v, MultipartFile videoData) throws IOException {
         videoDataMgr.saveVideoData(v, videoData.getInputStream());
    }

    private void serveVideo(Video v, HttpServletResponse response) throws IOException {
         // TODO send headers, etc. to the client
         videoDataMgr.copyVideoData(v, response.getOutputStream());
    }
	
}
