package exploresurvival.game.gui;

import exploresurvival.game.ExploreSurvival;
import exploresurvival.game.render.ShapeRenderer;
import exploresurvival.game.util.ThreadFetchMessage;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class GuiMainMenu extends GuiScreen {

	private String splashText = "ExploreSurvival!";

	int posY = 30;
	int posX = 27;
	int scoll = 0;

   
    /**
     * @param hsl
     * @return
     */
    public static int HSL2RGB(float H,float S,float L) {
   
        float var_1, var_2; 
        int R, G, B;
        if (S == 0) {  
            R = (int) L;  
            G = (int) L;  
            B = (int) L;  
        } else {  
            if (L < 128) {  
                var_2 = (L * (256 + S)) / 256;  
            } else {  
                var_2 = (L + S) - (S * L) / 256;  
            }  
            if (var_2 > 255) {  
                var_2 = Math.round(var_2);  
            }  
            if (var_2 > 254) {  
                var_2 = 255;  
            }  
            var_1 = 2 * L - var_2;  
            R = Math.round(RGBFromHue(var_1, var_2, H + 120))<<16;  
            G = Math.round(RGBFromHue(var_1, var_2, H))<<8;  
            B = Math.round(RGBFromHue(var_1, var_2, H - 120));  
        }  
        return R^G^B;
    }  
   
    /**
     * @param a
     * @param b
     * @param h
     * @return
     */
    public static float RGBFromHue(float a, float b, float h) {  
        if (h < 0) {  
            h += 360;  
        }  
        if (h >= 360) {  
            h -= 360;  
        }  
        if (h < 60) {  
            return a + ((b - a) * h) / 60;  
        }  
        if (h < 180) {  
            return b;  
        }  
   
        if (h < 240) {  
            return a + ((b - a) * (240 - h)) / 60;  
        }  
        return a;  
    }  
     

	public GuiMainMenu(ExploreSurvival game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	public static ArrayList<String> messages = new ArrayList<String>();
	int tick = 0;

	public void tick(float passedTime) {
		tick++;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		controls.add(new Button(game, 0, 10, 100, "Play"));
		controls.add(new Button(game, 1, 10, 125, "Settings"));
		controls.add(new Button(game, 2, 10, 150, "Tutorial"));
		controls.add(new Button(game, 3, 10, 175, "Exit Game"));

		((Button) controls.get(0)).enable = false;
		((Button) controls.get(2)).enable = false;
		new ThreadFetchMessage(this).start();
		if (messages != null && (width - 20) - (posX + 200) > 8) {
			msg.clear();
			for (String s : messages) {
				addLine(s);
			}
		}
	}

	public ArrayList<String> msg = new ArrayList<String>();

	public void addLine(String s) {
		int w = (width - 20) - (posX + 200);
		w -= w % 8;
		if (w < 8)
			return;
		int i;
		for (; game.fontRenderer.getWidth(s) > w; s = s.substring(i)) {
			for (i = 1; i < s.length() && game.fontRenderer.getWidth(s.substring(0, i + 1)) <= w; i++) {
			}
			addLine(s.substring(0, i));
		}
		msg.add(s);
	}

	@Override
	public void render(int mouseX, int mouseY) {
		super.render(mouseX, mouseY);
		// user
		int color=game.session.isOffline ? 0xFFFFFF : HSL2RGB(tick%360,255,127);
		//this.drawCenteredString(game.fontRenderer, "Welcome! " + game.session.username, 50, 205, color);
		game.fontRenderer.render("Welcome! ", 10, 200, 0xFFFFFF);
		game.fontRenderer.render(game.session.username, 10+game.fontRenderer.getWidth("Welcome! "), 200, color);
		// logo
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, game.renderEngine.getTexture("/logo.png"));
		ShapeRenderer s = ShapeRenderer.instance;
		GL11.glPushMatrix();
		s.begin();
		s.vertexUV(posX, posY, 0, 0, 0);
		s.vertexUV(posX + 331 / 2, posY, 0, 1, 0);
		s.vertexUV(posX + 331 / 2, posY + 130 / 2, 0, 1, 1);
		s.vertexUV(posX, posY + 130 / 2, 0, 0, 1);
		s.end();
		GL11.glPopMatrix();
		if (msg.size() > 0) {
			GL11.glPushMatrix();
			int w = (width - 20) - (posX + 200);
			int h = (height - 40) - posY;
			w -= w % 8;
			h -= h % 8;
			int y = posY;
			drawRect(posX + 200 - 2, posY - 2, posX + 200 + w + 2, posY + h + 2, 0xaa000000);
			GL11.glEnable(GL11.GL_BLEND);
			int wheel = scoll;
			synchronized (msg) {
				for (String str : msg) {
					if (wheel-- > 0)
						continue;
					game.fontRenderer.render(str, posX + 200, y, 0xFFFFFF);
					y += 8;
				}
			}
			GL11.glPopMatrix();
		}
	}

	@Override
	public void onButtonClick(Button button) {
		// TODO Auto-generated method stub
		if (button.id == 1) {
			game.setCurrentScreen(new GuiSettings(game, this));
		}
		if (button.id == 3) {
			game.running = false;
		}
	}

	public void mouseEvent() {
		super.mouseEvent();
		int wheel = Mouse.getDWheel();
		if (wheel == 0)
			return;
		if (scoll > 0 && wheel > 0) {
			scoll--;
		}
		if (wheel < 0) {
			scoll++;
		}
	}

}
