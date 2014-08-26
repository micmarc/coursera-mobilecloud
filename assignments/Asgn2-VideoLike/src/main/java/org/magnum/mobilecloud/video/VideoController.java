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

package org.magnum.mobilecloud.video;

import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
public class VideoController {

	private static final String ID_PARAM = "id";
	private static final String VIDEO_PATH = VideoSvcApi.VIDEO_SVC_PATH;
	private static final String VIDEO_ID_PATH = VIDEO_PATH + "/{" + ID_PARAM
			+ "}";

	@Autowired
	private VideoRepository videos;

	// POST /video
	@RequestMapping(value = VIDEO_PATH, method = RequestMethod.POST)
	public @ResponseBody Video addVideo(@RequestBody Video v) {
		Video savedVideo = videos.save(v);
		return savedVideo;
	}

	// GET /video
	@RequestMapping(value = VIDEO_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<Video> getVideoList() {
		Iterable<Video> results = videos.findAll();
		return Lists.newArrayList(results);
	}

	// GET /video/{id}
	@RequestMapping(value = VIDEO_ID_PATH, method = RequestMethod.GET)
	public @ResponseBody Video getVideo(@PathVariable(ID_PARAM) long id,
			HttpServletResponse response) {

		Video v = getVideoWithResponse(id, response);

		return v;
	}

	// POST /video/{id}/like
	@RequestMapping(value = VIDEO_ID_PATH + "/like", method = RequestMethod.POST)
	public @ResponseBody void likeVideo(@PathVariable(ID_PARAM) long id,
			Principal p, HttpServletResponse response) {

		Video v = getVideoWithResponse(id, response);
		if (v == null) {
			// Video not found
			return;
		}

		String user = p.getName();
		if (!tryOperation(v.like(user), response)) {
			// User has already liked this video
			return;
		}

		// All good
		videos.save(v);
	}

	// POST /video/{id}/unlike
	@RequestMapping(value = VIDEO_ID_PATH + "/unlike", method = RequestMethod.POST)
	public @ResponseBody void unlikeVideo(@PathVariable(ID_PARAM) long id,
			Principal p, HttpServletResponse response) {

		Video v = getVideoWithResponse(id, response);
		if (v == null) {
			// Video not found
			return;
		}

		String user = p.getName();
		if (!tryOperation(v.unlike(user), response)) {
			// User does not already like this video
			return;
		}

		// All good
		videos.save(v);
	}
	
	// GET /video/{id}/likedby
	@RequestMapping(value = VIDEO_ID_PATH + "/likedby", method = RequestMethod.GET)
	public @ResponseBody Collection<String> getVideoLikedBy(@PathVariable(ID_PARAM) long id,
			HttpServletResponse response) {

		Video v = getVideoWithResponse(id, response);
		
		return v == null ? null : v.getLikedBy();
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_TITLE_SEARCH_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<Video> findByTitle(
			@RequestParam(VideoSvcApi.TITLE_PARAMETER) String title) {

		Collection<Video> results = videos.findByName(title);
		return results;

	}

	@RequestMapping(value = VideoSvcApi.VIDEO_DURATION_SEARCH_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<Video> findByDurationLessThan(
			@RequestParam(VideoSvcApi.DURATION_PARAMETER) long maxDuration) {

		Collection<Video> results = videos.findByDurationLessThan(maxDuration);

		return results;
	}

	private Video getVideoWithResponse(long id, final HttpServletResponse response) {
		Video v = videos.findOne(id);
		if (v == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		
		return v;
	}

	private boolean tryOperation(boolean result, final HttpServletResponse response) {
		if (!result) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		return result;
	}

}
