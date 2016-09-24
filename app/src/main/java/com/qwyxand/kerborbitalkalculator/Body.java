package com.qwyxand.kerborbitalkalculator;

/** Body
 * Stores information about one of the bodies in the Kerbol system (either a planet or the star Kerbol)
 * Class stores final variables, essentially acting as a C struct
 *
 * name stores the name of the body
 * mu stores the body's standard gravitational parameter
 * sma stores the body's semi-major axis
 * radius stores the body's radius
 * soi stores the body's sphere of influence
 * color stores the color the body will be drawn in on the display views
 */
class Body {
    final String name;
    final float mu;
    final float sma;
    final int radius;
    final float soi;
    final int color;

    Body(String _name, float _mu, float _sma, int _radius, float _soi, int _color) {
        name = _name;
        mu = _mu;
        sma = _sma;
        radius = _radius;
        soi = _soi;
        color = _color;
    }
}