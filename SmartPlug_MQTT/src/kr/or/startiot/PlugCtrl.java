package kr.or.startiot;
import java.io.IOException;
import java.util.Map;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import comus.wp.onem2m.common.enums.M2MCmdType;
import comus.wp.onem2m.common.enums.M2MExecModeType;
import comus.wp.onem2m.common.vo.ln.AnyArgType;
import comus.wp.onem2m.iwf.common.M2MException;
import comus.wp.onem2m.iwf.nch.NotifyResponse;
import comus.wp.onem2m.iwf.run.CmdListener;
import comus.wp.onem2m.iwf.run.IWF;

public class PlugCtrl {

	public static void main(String[] args) throws IOException {
		IWF device = null;
		Runtime rt2 = Runtime.getRuntime();
		rt2.exec("gpio mode 0 out");
		try {
			device = new IWF("12345.1234.RP123");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (M2MException e) {
			e.printStackTrace();
		}

		device.register();

		AnyArgType any1 = new AnyArgType();
		any1.setName("switch");
		any1.setValue("initial_value");
		try {
			device.putControl("control", M2MCmdType.DOWNLOAD, M2MExecModeType.IMMEDIATEONCE, any1);
		} catch (M2MException e) {
			e.printStackTrace();
		}


		device.addCmdListener(new CmdListener() {
			String power;

			@Override
			public void excute(Map<String, String> cmd, NotifyResponse response) {
				try {
					power = cmd.get("switch");
					System.out.println("switch : " + power);
					Runtime rt= Runtime.getRuntime();
					if ("OFF".equals(power)) {
						rt.exec("gpio write 0 0");
					} else if ("ON".equals(power)) {
						rt.exec("gpio write 0 1");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}


			}
		});

	}

}
