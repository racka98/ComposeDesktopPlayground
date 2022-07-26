package skia

import org.intellij.lang.annotations.Language

@Language("GLSL")
val compositeSksl = """
    uniform shader content;
    uniform shader blur;
    uniform shader noise;

    uniform vec4 rectangle;
    uniform float radius;
    uniform float dropShadowSize;

    // Simplified version of SDF (signed distance function) for a rounded box
    // from https://www.iquilezles.org/www/articles/distfunctions2d/distfunctions2d.htm
    float roundedRectangleSDF(vec2 position, vec2 box, float radius) {
        vec2 q = abs(position) - box + vec2(radius);
        return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - radius;
    }

    vec4 main(vec2 coord) {
        vec2 shiftRect = (rectangle.zw - rectangle.xy) / 2.0;
        vec2 shiftCoord = coord - rectangle.xy;
        float distanceToClosestEdge = roundedRectangleSDF(
            shiftCoord - shiftRect, shiftRect, radius);

        vec4 c = content.eval(coord);
        if (distanceToClosestEdge > 0.0) {
            // We're outside of the filtered area
            if (distanceToClosestEdge < dropShadowSize) {
                // Emulate drop shadow around the filtered area
                float darkenFactor = (dropShadowSize - distanceToClosestEdge) / dropShadowSize;
                // Use exponential drop shadow decay for more pleasant visuals
                darkenFactor = pow(darkenFactor, 1.6);
                // Shift towards black, by 10% around the edge, dissipating to 0% further away
                return c * (0.9 + (1.0 - darkenFactor) / 10.0);
            }
            return c;
        }

        vec4 b = blur.eval(coord);
        vec4 n = noise.eval(coord);
        // How far are we from the top-left corner?
        float lightenFactor = min(1.0, length(coord - rectangle.xy) / (0.85 * length(rectangle.zw - rectangle.xy)));
        // Add some noise for extra texture
        float noiseLuminance = dot(n.rgb, vec3(0.2126, 0.7152, 0.0722));
        lightenFactor = min(1.0, lightenFactor + noiseLuminance);
        // Shift towards white, by 35% in top left corner, down to 10% in bottom right corner
        return b + (vec4(1.0) - b) * (0.35 - 0.25 * lightenFactor);
    }
"""