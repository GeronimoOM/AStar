import org.lwjgl.input.Keyboard;


public class ButtonWrap {
	boolean buttonDown;
	int button;
	
	public ButtonWrap(int button){
		this.button = button;
	}
	
	public boolean isPushed(){
		if(Keyboard.isKeyDown(button)){
    		if(!buttonDown) {
    			buttonDown = true;
    			return true;
    		}
    		return false;
    	}
		buttonDown = false;
		return false;
	}
	
}
