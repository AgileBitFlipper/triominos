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
 * Last modified: 11/30/17 4:59 PM
 */

package com.thirdsonsoftware

class FaceTest extends GroovyTestCase {

    Face oneFace = null
    Face twoFace = null
    Face threeFace = null
    Face fourFace = null

    void setUp() {
        super.setUp()

        oneFace = new Face(1,2)
        twoFace = new Face(4,5)
        threeFace = new Face(5,4)
        fourFace = new Face(1,2)
    }

    void tearDown() {
        oneFace = null
        twoFace = null
        threeFace = null
        fourFace = null
    }

    void testToString() {
        String strFace = oneFace
        assertEquals("1-2",strFace)
        strFace = twoFace
        assertEquals( "4-5",strFace)
    }

    void testMatch() {
        assertTrue(twoFace.match(threeFace))
    }

    void testNoMatch() {
        assertFalse(oneFace.match(twoFace))
    }

    void testCompareToEquals() {
        assertEquals(0, oneFace <=> fourFace)
    }

    void testCompareToLessThan() {
        assertEquals(-1, oneFace <=> twoFace)
    }

    void testCompareToMoreThan() {
        assertEquals(1, twoFace <=> oneFace)
    }
}
