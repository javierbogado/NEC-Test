package org.nec.test.rest;

import org.springframework.stereotype.Service;

@Service("finger_endpoints")
public class FingersImp implements Fingers {

	@Override
	public Object fingerPrint(String body) {
		return null;
	}

}
