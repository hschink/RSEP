package org.iti.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class MarkerUtils {

	public static Object getMarkerAttribute(IMarker marker, String attributeName) {
		
		try {
			return marker.getAttribute(attributeName);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String getMarkerType(IMarker marker) {
		try {
			return marker.getType();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}

	public static void deleteMarkers(List<IMarker> markers) {
		for (IMarker marker : markers) {
			MarkerUtils.deleteMarker(marker);
		}
	}
	
	public static void deleteMarker(IMarker marker) {
		try {
			marker.delete();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static IMarker createMarker(IResource resource, String id) {
		try {
			return resource.createMarker(id);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public static void setAttribute(IMarker marker, String attributeName, Object value) {
		try {
			marker.setAttribute(attributeName, value);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static IMarker getMarkerByPosition(List<IMarker> markers, int position, String positionId) {
		
		for (IMarker marker : markers) {
			int pos = (int)MarkerUtils.getMarkerAttribute(marker, positionId);
			
			if (pos == position)
				return marker;
		}
		
		return null;
	}

	public static List<IMarker> getMarkersInProject(IProject project, String bindingType) {
		List<IMarker> markers = new ArrayList<>();
		
		try {
			markers.addAll(Arrays.asList(project.findMarkers(bindingType, true, IResource.DEPTH_INFINITE)));
		} catch (CoreException e) { }
		
		return markers;
	}
}
