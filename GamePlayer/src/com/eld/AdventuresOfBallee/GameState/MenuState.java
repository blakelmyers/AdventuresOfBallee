// The main menu GameState.

package com.eld.AdventuresOfBallee.GameState;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.studiohartman.jamepad.*;

import com.eld.AdventuresOfBallee.Manager.Content;
import com.eld.AdventuresOfBallee.Manager.GameStateManager;
import com.eld.AdventuresOfBallee.Manager.JukeBox;
import com.eld.AdventuresOfBallee.Manager.Keys;

public class MenuState extends GameState {
	
	private BufferedImage bg;
	private BufferedImage diamond;
	private ControllerManager controllers;
	
	private int currentOption = 0;
	private String[] options = {
		"START",
		"QUIT"
	};
	
	public MenuState(GameStateManager gsm) {
		super(gsm);
	}
	
	public void init() {
		bg = Content.MENUBG[0][0];
		diamond = Content.DIAMOND[0][0];
		JukeBox.load("/SFX/collect.wav", "collect");
		JukeBox.load("/SFX/menuoption.wav", "menuoption");

		controllers = new ControllerManager();
		controllers.initSDLGamepad();
	}
	
	public void update() {
		handleInput();
	}
	
	public void draw(Graphics2D g) {
		
		g.drawImage(bg, 0, 0, null);
		
		Content.drawString(g, options[0], 44, 100);
		Content.drawString(g, options[1], 48, 110);
		
		if(currentOption == 0) g.drawImage(diamond, 25, 96, null);
		else if(currentOption == 1) g.drawImage(diamond, 25, 106, null);
		
	}
	
	public void handleInput() {

		ControllerIndex currController = controllers.getControllerIndex(0);

		ControllerState currState = controllers.getState(0);

		controllers.update();
		boolean upStick = false;
		boolean downStick = false;
		boolean aButton = false;

		try {
			if(currController.isButtonPressed(ControllerButton.A)) {
				aButton = true;
			}
			if(currController.isButtonPressed(ControllerButton.B)) {
				System.out.println("\"B\" on \"" + currController.getName() + "\" is pressed");
			}

			if(currController.getAxisState(ControllerAxis.LEFTY) > 0.9f) {
				upStick = true;
			}
			else if(currController.getAxisState(ControllerAxis.LEFTY) < - 0.9f) {
				downStick = true;
			}
		} catch (ControllerUnpluggedException e) {
			// do nothing
		}

		if((Keys.isPressed(Keys.DOWN) || downStick) && currentOption < options.length - 1) {
			JukeBox.play("menuoption");
			currentOption++;
		}
		if((Keys.isPressed(Keys.UP) || upStick) && currentOption > 0) {
			JukeBox.play("menuoption");
			currentOption--;
		}
		if(Keys.isPressed(Keys.ENTER) || aButton) {
			JukeBox.play("collect");
			selectOption();
		}
	}
	
	private void selectOption() {
		if(currentOption == 0) {
			gsm.setState(GameStateManager.PLAY);
		}
		if(currentOption == 1) {
			System.exit(0);
		}
	}
	
}
