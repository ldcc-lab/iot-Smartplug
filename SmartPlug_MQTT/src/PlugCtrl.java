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

	public static void main(String[] args) {

		System.out.println("system started");

		final GpioController gpio = GpioFactory.getInstance();

		final GpioPinDigitalOutput light_pinOut1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "POWER", PinState.LOW);

		IWF device = null;
		try {
			device = new IWF("55555.5555.RP09");
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

		/*
		 * device.putContent("sensor1", "2223");
		 * device.putBattery(M2MBatteryStatus.CHARGING_COMPLETE, 50);
		 * device.putDeviceInfo("deviceLabel", "manufacturer", "model",
		 * "deviceTye", "fwVersion", "swVersion", "hwVersion");
		 * device.putFirmware("version", "firmwareName", "URL", true);
		 * device.putMemory(100, 50); device.putSoftware("version",
		 * "softwareName", "URL"); device.putReboot(false, true);
		 * device.setStatus("try");
		 */

		device.addCmdListener(new CmdListener() {
			String power;

			@Override
			public void excute(Map<String, String> cmd, NotifyResponse response) {
				try {
					power = cmd.get("switch");
					System.out.println("switch : " + power);

				} catch (Exception e) {
					e.printStackTrace();
				}

				if ("OFF".equals(power)) {
					light_pinOut1.high();
				} else if ("ON".equals(power)) {
					light_pinOut1.low();
				}
			}
		});

	}

}
