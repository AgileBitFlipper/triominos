/*
 * Copyright (c) 2017 Third Son Software, LLC
 * (http://thirdsonsoftware.com/) and others. All rights reserved.
 * Created by Andrew B. Montcrieff on 11/30/17 5:08 PM.
 *
 * This file is part of Project triominos.
 *
 * triominos can not be copied and/or distributed without the express
 * permission of Andrew B. Montcrieff or Third Son Software, LLC.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified: 11/30/17 5:07 PM
 */

package com.thirdsonsoftware;

import java.io.Serializable;

/** Represents a single face of a tile.
 * @author Andrew B. Montcrieff
 * @author www.ThirdSonSoftware.com
 * @version 0.1
 * @since 0.0
 */
public class Face implements Comparable, Serializable {

    // Left corner facing away from tile face
    private final int left;

    // Right corner facing away from tile face
    private final int right;

    /** General constructor for a Face object
     * @param l - Outward facing left-hand value for face
     * @param r - Outward facing right-hand value for face
     */
    public Face(int l, int r) {
        left = l;
        right = r;
    }

    /** Returns the string that shows the face
     * @return - String defining the face
     */
    public String toString() {
        return String.format("%d-%d", left, right );
    }

    /** Determines if the current face could be adjacent to the one provided
     * @param o - Face to determine if a match exists
     * @return - true if faces can be matched, false otherwise
     */
    public boolean match(Object o) {
        Face f = (Face)o;
        return ( ( left == f.right ) && ( right == f.left ) );
    }

    @Override
    public boolean equals(Object o) {
        Face f = (Face)o;
        if ( ( left == f.left ) && ( right == f.right ) )
            return true;
        return false;
    }

    /** Compare two faces to determine if they are the same
     * @param o - Face to compare this Face with.
     * @return - -1 if a corner of a face is less than the matching corner of the other face
     *            1 if a corner of a face is more than the matching corner of the other face
     *            0 if both corners on both faces match their respective corners
     * @apiNote - This is not a 'match' comparison to see if they can be played next to each
     *            other.  This API determines if the two faces have equal value corresponding
     *            corners.
     */
    @Override
    public int compareTo(Object o) {
        Face f = (Face)o;

        if ( ( left < f.left ) || ( right < f.right ) )
            return -1;
        else if ( ( left > f.left ) || ( right > f.right ) )
            return 1;
        else
            return 0;
    }
}
