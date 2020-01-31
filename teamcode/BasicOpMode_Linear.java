/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;




/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 * <p>
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name = "Sagittarius", group = "Linear Opmode")
public class BasicOpMode_Linear extends LinearOpMode {

    // Declare OpMode members.
    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor leftDrive = null;
    public DcMotor rightDrive = null;
    public DcMotor midDrive = null;//
    public DcMotor pulleyVertical = null;
    public DcMotor pulleyHorizontal = null;
    public Servo OpenGripServo = null;
    public Servo CloseGripServo = null;
    public Servo CapServo = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        midDrive = hardwareMap.get(DcMotor.class, "mid_drive");
        pulleyVertical = hardwareMap.get(DcMotor.class, "pulley_Vertical");
        pulleyHorizontal = hardwareMap.get(DcMotor.class, "pulley_Horizontal");
        //rightClaw = hardwareMap.get(Servo.class, "rightClaw");
        //leftClaw = hardwareMap.get(Servo.class, "leftClaw");

        /* I separated the Grip Servo into two variables (Open and Close)
        I used it to prevent interference with closing/opening of the Grip Servo whenever we would refer to them.
        So, technically OpenGripServo and CloseGripServo do the same thing, but it is a helpful quality of life imo */
        OpenGripServo = hardwareMap.get(Servo.class, "gripServo1");
        CloseGripServo = hardwareMap.get(Servo.class, "gripServo2");
        CapServo = hardwareMap.get (Servo.class, "capServo");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        pulleyVertical.setDirection(DcMotor.Direction.REVERSE);
        pulleyHorizontal.setDirection(DcMotor.Direction.REVERSE);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        OpenGripServo.setPosition(0);
        CloseGripServo.setPosition(0);
        CapServo.setPosition(.3);


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;
            //double midPower;
            double HpulleyPower;
            double VpulleyPower;
            //double gripDir = 0;

            leftPower = gamepad1.left_stick_y;
            rightPower = gamepad1.right_stick_y;
            //midPower = 0;
            HpulleyPower = gamepad2.right_stick_y;
            VpulleyPower = gamepad2.left_stick_y;

            // Send calculated power to wheels
            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);
            //midDrive.setPower(midPower);
            pulleyHorizontal.setPower(HpulleyPower);
            pulleyVertical.setPower(VpulleyPower);

            //Servos Start Here
            /*IMPORTANT SERVO UPDATE PLEASE READ.
            Here is the discovery: so unlike last year, whenever we setPosition a servo, it does not actually set a position.
            Instead, the values we put behave a lot more like motors as it moves a direction continuously.
            When we set it "position" to a value of 1, the servo moves one direction forever; set it to 0, the servo moves the other; set it to .5, it stops.
            Idk how this happened, but I believe the cause to be the time we were configuring and setting up the Rev Servos.
            Also, I tried making the controls and programming easier and smooth by adding ELSE statements, however it was being slow because I believe the ping between the phones and controller is too high
            */


            //Controlling the RIGHT/LEFT claw and Capping mechanism


            if (gamepad2.left_bumper) {
                OpenGripServo.setPosition(0.5);
            } else{
                OpenGripServo.setPosition(0);
            }

            if (gamepad2.right_bumper) {
                CloseGripServo.setPosition(0);
            } else{
                CloseGripServo.setPosition(0.5);
            }


            if (gamepad2.a) {
                CapServo.setPosition(0.3);
            }

            if (gamepad2.b) {
                CapServo.setPosition(0);
            }
            if (gamepad2.x) {
                CapServo.setPosition(1);
            }







            //CONTROLLING MIDDRIVE
            if (gamepad1.dpad_left) {
                midDrive.setPower(1);
            } else if (gamepad1.dpad_right){
                midDrive.setPower(-1);
            } else{
                midDrive.setPower(0);
            }


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            //telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftDrive, rightDrive);
            telemetry.update();
        }


    }
}



