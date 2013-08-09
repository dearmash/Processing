
package clockharness.util;

import static java.lang.Math.*;

public class Easing {

    /**
     * Simple linear tweening - No easing
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float linearTween(float t, float b, float c, float d) {
        return c * t / d + b;
    }

    /* QUADRATIC EASING: t^2 */

    /**
     * Quadradic easing in - accelerating from zero
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInQuad(float t, float b, float c, float d) {
        return c * (t /= d) * t + b;
    }

    /**
     * Quadradic easing out - decelerating to zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeOutQuad(float t, float b, float c, float d) {
        return -c * (t /= d) * (t - 2) + b;
    }

    /**
     * Quadradic easing in/out - acceleration until halfway, then deceleration
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInOutQuad(float t, float b, float c, float d) {
        if ((t /= d / 2) < 1)
            return c / 2 * t * t + b;
        return -c / 2 * ((--t) * (t - 2) - 1) + b;
    }

    /* CUBIC EASING: t^3 */

    /**
     * Cubic easing in - accelerating from zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInCubic(float t, float b, float c, float d) {
        return c * (t /= d) * t * t + b;
    }

    /**
     * Cubic easing out - decelerating to zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeOutCubic(float t, float b, float c, float d) {
        return c * ((t = t / d - 1) * t * t + 1) + b;
    }

    /**
     * Cubic easing in/out - acceleration until halfway, then deceleration
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInOutCubic(float t, float b, float c, float d) {
        if ((t /= d / 2) < 1)
            return c / 2 * t * t * t + b;
        return c / 2 * ((t -= 2) * t * t + 2) + b;
    }

    /* QUARTIC EASING: t^4 */

    /**
     * Quartic easing in - accelerating from zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInQuart(float t, float b, float c, float d) {
        return c * (t /= d) * t * t * t + b;
    }

    /**
     * Quartic easing out - decelerating to zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeOutQuart(float t, float b, float c, float d) {
        return -c * ((t = t / d - 1) * t * t * t - 1) + b;
    }

    /**
     * Quartic easing in/out - acceleration until halfway, then deceleration
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInOutQuart(float t, float b, float c, float d) {
        if ((t /= d / 2) < 1)
            return c / 2 * t * t * t * t + b;
        return -c / 2 * ((t -= 2) * t * t * t - 2) + b;
    }

    /* QUINTIC EASING: t^5 */

    /**
     * Quintic easing in - accelerating from zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInQuint(float t, float b, float c, float d) {
        return c * (t /= d) * t * t * t * t + b;
    }

    /**
     * Quintic easing out - decelerating to zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeOutQuint(float t, float b, float c, float d) {
        return c * ((t = t / d - 1) * t * t * t * t + 1) + b;
    }

    /**
     * Quintic easing in/out - acceleration until halfway, then deceleration
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInOutQuint(float t, float b, float c, float d) {
        if ((t /= d / 2) < 1)
            return c / 2 * t * t * t * t * t + b;
        return c / 2 * ((t -= 2) * t * t * t * t + 2) + b;
    }

    /* SINUSOIDAL EASING: sin(t) */

    /**
     * Sinusoidal easing in - accelerating from zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInSine(float t, float b, float c, float d) {
        return (float) (-c * cos(t / d * (PI / 2)) + c + b);
    }

    /**
     * Sinusoidal easing out - decelerating to zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeOutSine(float t, float b, float c, float d) {
        return (float) (c * sin(t / d * (PI / 2)) + b);
    }

    /**
     * Sinusoidal easing in/out - accelerating until halfway, then decelerating
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInOutSine(float t, float b, float c, float d) {
        return (float) (-c / 2 * (cos(PI * t / d) - 1) + b);
    }

    /* EXPONENTIAL EASING: 2^t */

    /**
     * Exponential easing in - accelerating from zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInExpo(float t, float b, float c, float d) {
        return (float) ((t == 0) ? b : c * Math.pow(2, 10 * (t / d - 1)) + b);
    }

    /**
     * Exponential easing out - decelerating to zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeOutExpo(float t, float b, float c, float d) {
        return (float) ((t == d) ? b + c : c * (-pow(2, -10 * t / d) + 1) + b);
    }

    /**
     * Exponential easing in/out - accelerating until halfway, then decelerating
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInOutExpo(float t, float b, float c, float d) {
        if (t == 0)
            return b;
        if (t == d)
            return b + c;
        if ((t /= d / 2) < 1)
            return (float) (c / 2 * pow(2, 10 * (t - 1)) + b);
        return (float) (c / 2 * (-pow(2, -10 * --t) + 2) + b);
    }

    /* CIRCULAR EASING: sqrt(1-t^2) */

    /**
     * Circular easing in - accelerating from zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInCirc(float t, float b, float c, float d) {
        return (float) (-c * (sqrt(1 - (t /= d) * t) - 1) + b);
    }

    /**
     * Circular easing out - decelerating to zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeOutCirc(float t, float b, float c, float d) {
        return (float) (c * sqrt(1 - (t = t / d - 1) * t) + b);
    }

    /**
     * Circular easing in/out - acceleration until halfway, then deceleration
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInOutCirc(float t, float b, float c, float d) {
        if ((t /= d / 2) < 1)
            return (float) (-c / 2 * (sqrt(1 - t * t) - 1) + b);
        return (float) (c / 2 * (sqrt(1 - (t -= 2) * t) + 1) + b);
    }

    /* ELASTIC EASING: exponentially driving / decaying sine wave */

    /**
     * Elastic easing in - exponentially driving sine wave from zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInElastic(float t, float b, float c, float d) {
        return easeInElastic(t, b, c, d, 0);
    }

    /**
     * Elastic easing in - exponentially driving sine wave from zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @param a amplitude of wave
     * @return the "eased" value
     */
    public static float easeInElastic(float t, float b, float c, float d, float a) {
        return easeInElastic(t, b, c, d, a, d * 0.3f);
    }

    /**
     * Elastic easing in - exponentially driving sine wave from zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @param a amplitude of wave
     * @param p period of wave
     * @return the "eased" value
     */
    public static float easeInElastic(float t, float b, float c, float d, float a, float p) {
        float s;
        if (t == 0)
            return b;
        if ((t /= d) == 1)
            return b + c;
        if (a < abs(c)) {
            a = c;
            s = p / 4;
        } else
            s = (float) (p / (2 * PI) * asin(c / a));
        return (float) (-(a * pow(2, 10 * (t -= 1)) * sin((t * d - s) * (2 * PI) / p)) + b);
    }

    /**
     * Elastic easing in - exponentially decaying sine wave to zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeOutElastic(float t, float b, float c, float d) {
        return easeOutElastic(t, b, c, d, 0);
    }

    /**
     * Elastic easing in - exponentially decaying sine wave to zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @param a amplitude of wave
     * @return the "eased" value
     */
    public static float easeOutElastic(float t, float b, float c, float d, float a) {
        return easeOutElastic(t, b, c, d, a, d * 0.3f);
    }

    /**
     * Elastic easing in - exponentially decaying sine wave to zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @param a amplitude of wave
     * @param p period of wave
     * @return the "eased" value
     */
    public static float easeOutElastic(float t, float b, float c, float d, float a, float p) {
        float s;
        if (t == 0)
            return b;
        if ((t /= d) == 1)
            return b + c;

        if (a < abs(c)) {
            a = c;
            s = p / 4;
        } else
            s = (float) (p / (2 * PI) * asin(c / a));
        return (float) (a * pow(2, -10 * t) * sin((t * d - s) * (2 * PI) / p) + c + b);
    }

    /**
     * Elastic easing in - exponentially driving until halfway, then decaying
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInOutElastic(float t, float b, float c, float d) {
        return easeInOutElastic(t, b, c, d, 0);
    }

    /**
     * Elastic easing in - exponentially driving until halfway, then decaying
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @param a amplitude of wave
     * @return the "eased" value
     */
    public static float easeInOutElastic(float t, float b, float c, float d, float a) {
        return easeInOutElastic(t, b, c, d, a, d * (.3f * 1.5f));
    }

    /**
     * Elastic easing in - exponentially driving until halfway, then decaying
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @param a amplitude of wave
     * @param p period of wave
     * @return the "eased" value
     */
    public static float easeInOutElastic(float t, float b, float c, float d, float a, float p) {
        float s;
        if (t == 0)
            return b;
        if ((t /= d / 2) == 2)
            return b + c;

        if (a < abs(c)) {
            a = c;
            s = p / 4;
        } else
            s = (float) (p / (2 * PI) * asin(c / a));
        if (t < 1)
            return (float) (-.5 * (a * pow(2, 10 * (t -= 1)) * sin((t * d - s) * (2 * PI) / p)) + b);
        return (float) (a * pow(2, -10 * (t -= 1)) * sin((t * d - s) * (2 * PI) / p) * .5 + c + b);
    }

    /* BACK EASING: overshooting cubic easing: (s+1)*t^3 - s*t^2 */

    /**
     * Back easing in - cubic easing backtracking the entry
     * <p>
     * Overshoot amount corresponds to ~10%
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInBack(float t, float b, float c, float d) {
        return easeInBack(t, b, c, d, 1.70158f);
    }

    /**
     * Back easing in - cubic easing backtracking the entry
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @param s overshoot amount
     * @return the "eased" value
     */
    public static float easeInBack(float t, float b, float c, float d, float s) {
        return c * (t /= d) * t * ((s + 1) * t - s) + b;
    }

    /**
     * Back easing out - cubic easing overshooting and returning to target
     * <p>
     * Overshoot amount corresponds to ~10%
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeOutBack(float t, float b, float c, float d) {
        return easeOutBack(t, b, c, d, 1.70158f);
    }

    /**
     * Back easing out - cubic easing overshooting and returning to target
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @param s overshoot amount
     * @return the "eased" value
     */
    public static float easeOutBack(float t, float b, float c, float d, float s) {
        return c * ((t = t / d - 1) * t * ((s + 1) * t + s) + 1) + b;
    }

    /**
     * Back easing in/out - cubic easing backtracking the entry then
     * overshooting and returning to target
     * <p>
     * Overshoot amount corresponds to ~10%
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInOutBack(float t, float b, float c, float d) {
        return easeInOutBack(t, b, c, d, 1.70158f);
    }

    /**
     * Back easing in/out - cubic easing backtracking the entry then
     * overshooting and returning to target
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @param s the overshoot amount
     * @return the "eased" value
     */
    public static float easeInOutBack(float t, float b, float c, float d, float s) {
        if ((t /= d / 2) < 1)
            return c / 2 * (t * t * (((s *= (1.525)) + 1) * t - s)) + b;
        return c / 2 * ((t -= 2) * t * (((s *= (1.525)) + 1) * t + s) + 2) + b;
    }

    /* BOUNCE EASING: exponentially driving / decaying parabolic bounce */

    /**
     * Bounce easing in - exponentially driving bounce from zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInBounce(float t, float b, float c, float d) {
        return c - easeOutBounce(d - t, 0, c, d) + b;
    }

    /**
     * Bounce easing out - exponentially decaying bounce to zero velocity
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeOutBounce(float t, float b, float c, float d) {
        if ((t /= d) < (1 / 2.75)) {
            return (float) (c * (7.5625 * t * t) + b);
        } else if (t < (2 / 2.75)) {
            return (float) (c * (7.5625 * (t -= (1.5 / 2.75)) * t + .75) + b);
        } else if (t < (2.5 / 2.75)) {
            return (float) (c * (7.5625 * (t -= (2.25 / 2.75)) * t + .9375) + b);
        } else {
            return (float) (c * (7.5625 * (t -= (2.625 / 2.75)) * t + .984375) + b);
        }
    }

    /**
     * Bounce easing in/out - exponentially driving, then decaying bounce
     *
     * @param t current time wrt duration
     * @param b start value
     * @param c change in value
     * @param d duration wrt current time
     * @return the "eased" value
     */
    public static float easeInOutBounce(float t, float b, float c, float d) {
        if (t < d / 2)
            return easeInBounce(t * 2, 0, c, d) * .5f + b;
        return easeOutBounce(t * 2 - d, 0, c, d) * .5f + c * .5f + b;
    }

}
