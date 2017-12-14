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
 * Last modified: 11/30/17 4:57 PM
 */

package com.thirdsonsoftware;

import java.io.*;

class Main {

    static Game game = null ;

    public static void main(String[] args) {

        game = new Game(2);

        game.play();

        Log.Info(game.toString());
    }

    // Todo: We should be able to save and retrieve game state...finish this later.

    /**
     * Save the game to a file
     */
    static protected void saveGame() {
        try {
            Log.Info("Saving the current game state...") ;
            FileOutputStream fos = new FileOutputStream("triominos.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(game);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load a game from a file
     * @return (Game) the game after loading from a saved state
     */
    static protected Game loadGame() {
        Game loadedGame = null;
        try {
            Log.Info("Loading game from saved state...");
            FileInputStream fis = new FileInputStream("triominos.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            loadedGame = (Game) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loadedGame;
    }
}
