package skia

import org.intellij.lang.annotations.Language

@Language("GLSL")
val compositeSksl = """
    uniform shader content;
    uniform shader blur;

    uniform vec4 rectangle;
    uniform float radius;

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
            return c;
        }

        vec4 b = blur.eval(coord);
        return b;
    }
"""