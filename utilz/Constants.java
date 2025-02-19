package utilz;

import main.Game;

public class Constants {

    public static class EnemyConstants{
        public static final int ROBOT = 0;

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int ROBOT_WIDTH_DEFAULT = 96;
        public static final int ROBOT_HEIGHT_DEFAULT = 55;

        public static final int ROBOT_WIDTH = (int)(96 * Game.SCALE);
        public static final int ROBOT_HEIGHT = (int)(55 * Game.SCALE);

        public static final int ROBOT_DEAWOFFSET_X = (int)(19 * Game.SCALE);
        public static final int ROBOT_DEAWOFFSET_Y = (int)(6 * Game.SCALE);

        public static int GetSpriteAmount(int enemy_tipe, int enemy_state){
            switch (enemy_tipe){
                case ROBOT:
                    switch (enemy_state){
                        case IDLE:
                            return 4;
                        case RUNNING:
                            return 6;
                        case ATTACK:
                            return 6;
                        case HIT:
                            return 2;
                        case DEAD:
                            return 6;
                    }
            }
            return 0;
        }

        public static int GetMaxHealth(int enemy_type){
            switch (enemy_type){
                case ROBOT:
                    return 10;
                default:
                    return 0;
            }
        }

        public static int GetEnemyDMG(int enemy_type){
            switch (enemy_type){
                case ROBOT:
                    return 20;
                default:
                    return 0;
            }
        }

    }

    public static class UI{
        public static class MenuButtons {
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
        }
        public static class PauseButtons{
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int)(SOUND_SIZE_DEFAULT * Game.SCALE);
        }
        public static class UrmButtons{
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int)(URM_DEFAULT_SIZE * Game.SCALE);
        }

        public static class VolumeButtons{
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH =(int)( VOLUME_DEFAULT_WIDTH * Game.SCALE );
            public static final int VOLUME_HEIGHT =(int)( VOLUME_DEFAULT_HEIGHT * Game.SCALE );
            public static final int SLIDER_WIDTH =(int)( SLIDER_DEFAULT_WIDTH * Game.SCALE );



        }
    }

    public static class Directions{
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;


    }


    public static class PlayerConstants{
        public static final int DEATH = 0;
        public static final int IDLE = 1;
        public static final int JUMP = 2;
        public static final int WALK = 3;
        public static final int ATTACK1 = 4;
        public static final int ATTACK2 = 5;
        public static final int ATTACK3 = 6;
        public static final int HURT = 7;


        public static int GetSpriteAount(int player_action){

            switch(player_action){
                case DEATH:
                    return 3;
                case IDLE:
                    return 8;
                case JUMP:
                    return 13;
                case WALK:
                    return 8;
                case ATTACK1:
                    return 4;
                case ATTACK2:
                    return 4;
                case ATTACK3:
                    return 5;
                case HURT:
                    return 6;
                default:
                    return 1;

            }

        }

    }

}
