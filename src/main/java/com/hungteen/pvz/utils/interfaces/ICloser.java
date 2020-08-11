package com.hungteen.pvz.utils.interfaces;

public interface ICloser {

	float getCloseWidth();
	
	float getCloseHeight();
	
	/**
	 * perform attack when target is close
	 */
	void performAttack();
	
	/**
	 * spawnParticle when death
	 */
	void spawnParticle();
}
