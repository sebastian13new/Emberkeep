package com.emberkeep.emberkeep.graphics;

import java.util.Map;
/**
 * Data pixel art semua sprite. Tiap huruf mewakili satu warna di palet,
 * titik berarti transparan.
 */
public final class SpriteData {
    private SpriteData() { }

    public static final Map<Character, Integer> JULIA_PALETTE = PixelArt.palette(
            'Y', 0xFFF2B33D, 'y', 0xFFC47F1A, 'c', 0xFF3BC9DB, 'o', 0xFF3A2230, 'H', 0xFF8B4F2A, 'h', 0xFF5E321A, 'j', 0xFFA8653A, 'S', 0xFFF6D2B4, 's', 0xFFDDA788, 'E', 0xFF2A1F33, 'e', 0xFFFFFFFF, 'm', 0xFFC4607A, 'P', 0xFFE86A92, 'p', 0xFFB9446E, 'L', 0xFFF7A1BE, 'q', 0xFF8E2F55, 'W', 0xFFFBF6EF, 'w', 0xFFD9CFE0, 'B', 0xFF6C5A9E, 'b', 0xFF47396E);

    public static final String[] JULIA_DOWN = {
            "......Y..YY..Y......",
            "......YY.YY.YY......",
            "......YYYccYYY......",
            ".....oyyyyyyyyo.....",
            "....oHHjjHHjjHHo....",
            "...oHHHHHHHHHHHHo...",
            "...oHHHSSSSSSHHHo...",
            "..oHHHSSSSSSSSHHHo..",
            "..oHHSSeESSeESSHHo..",
            "..oHHSSEESSEESSHHo..",
            "..oHhSSSSSSSSSShHo..",
            "..oHhhSSSmmSSShhHo..",
            "..oHhh.sSSSSs.hhHo..",
            "...oh.oWWWWWWo.ho...",
            ".....oPPLPPLPPo.....",
            "....oPPPLPPLPPPo....",
            "...osPPPPWWPPPPso...",
            "...osPPPPPPPPPPso...",
            "....opPPPPPPPPpo....",
            "....opPPLPPLPPpo....",
            "...opPPPLPPLPPPpo...",
            "...oppPPPPPPPPppo...",
            "..oqppppppppppppqo..",
            "...oqqqqqqqqqqqqo..."
    };
    public static final String[] JULIA_UP = {
            "......Y..YY..Y......",
            "......YY.YY.YY......",
            "......YYYYYYYY......",
            ".....oyyyyyyyyo.....",
            "....oHHjjHHjjHHo....",
            "...oHHHHHHHHHHHHo...",
            "...oHHHjHHHHjHHHo...",
            "..oHHHHHHHHHHHHHHo..",
            "..oHHjHHHHHHHHjHHo..",
            "..oHHHHHHHHHHHHHHo..",
            "..oHhHHHHHHHHHHhHo..",
            "..oHhhHHHHHHHHhhHo..",
            "..oHhhhHHHHHHhhhHo..",
            "...ohhhhhhhhhhhho...",
            ".....oPPPPPPPPo.....",
            "....oPPPWWWWPPPo....",
            "...osPPPPWWPPPPso...",
            "...osPPPPPPPPPPso...",
            "....opPPPPPPPPpo....",
            "....opPPPPPPPPpo....",
            "...opPPPPPPPPPPpo...",
            "...oppPPPPPPPPppo...",
            "..oqppppppppppppqo..",
            "...oqqqqqqqqqqqqo..."
    };
    public static final String[] JULIA_SIDE = {
            ".......Y..YY..Y.....",
            ".......YY.YY.YY.....",
            ".......YYYccYYY.....",
            "......oyyyyyyyyo....",
            ".....oHHjjHHjjHo....",
            "....oHHHHHHHHHHHo...",
            "...oHHHHHHSSSSSHo...",
            "...oHHHHHSSSSSSSo...",
            "...oHHHHHSSSSeESo...",
            "...oHHHHHSSSSEESo...",
            "...oHhHHHSSSSSSSso..",
            "...oHhhHHSSSSSmSo...",
            "...oHhhhHosSSSso....",
            "....ohhhhoWWWWo.....",
            "......oPPPLPPo......",
            ".....oPPPPLPPPo.....",
            ".....oPPPPPPPsso....",
            ".....oPPPPPPPsso....",
            ".....opPPPPPPPo.....",
            ".....opPPLPPPpo.....",
            "....opPPPLPPPPpo....",
            "....oppPPPPPPppo....",
            "...oqpppppppppppqo..",
            "....oqqqqqqqqqqqo..."
    };
    public static final String[] LEGS_IDLE = {
            ".....oBBo..oBBo.....",
            ".....obbo..obbo....."
    };
    public static final String[] LEGS_A = {
            "....oBBo....oBBo....",
            "....obbo......o....."
    };
    public static final String[] LEGS_B = {
            "....oBBo....oBBo....",
            ".....o......obbo...."
    };
    public static final String[] SIDE_LEGS_IDLE = {
            ".......oBBBBo.......",
            ".......obbbbo......."
    };
    public static final String[] SIDE_LEGS_A = {
            ".....oBBo..oBBo.....",
            ".....obbo...obbo...."
    };
    public static final String[] SIDE_LEGS_B = {
            "......oBBoBBo.......",
            "......obbobbo......."
    };

    public static final Map<Character, Integer> SWORD_PALETTE = PixelArt.palette(
            'Y', 0xFFF2B33D, 'y', 0xFFC47F1A, 'c', 0xFF3BC9DB, 'o', 0xFF3A2230, 'H', 0xFF8B4F2A, 'h', 0xFF5E321A, 'j', 0xFFA8653A, 'S', 0xFFF6D2B4, 's', 0xFFDDA788, 'E', 0xFF2A1F33, 'e', 0xFFFFFFFF, 'm', 0xFFC4607A, 'P', 0xFFE86A92, 'p', 0xFFB9446E, 'L', 0xFFF7A1BE, 'q', 0xFF8E2F55, 'W', 0xFFE8EEF6, 'w', 0xFF9FB0C6, 'B', 0xFF6C5A9E, 'b', 0xFF47396E);
    public static final String[] SWORD = {
            "..W..",
            ".WwW.",
            ".WwW.",
            ".WwW.",
            ".WwW.",
            ".WwW.",
            ".WwW.",
            ".WwW.",
            ".WwW.",
            ".WwW.",
            ".WwW.",
            "YYcYY",
            "yYYYy",
            "..h..",
            "..H..",
            "..Y.."
    };

    public static final Map<Character, Integer> SLIME_PALETTE = PixelArt.palette(
            'o', 0xFF123824, 'G', 0xFF38B26A, 'g', 0xFF237A4A, 'l', 0xFF9BE8B4, 'E', 0xFF10241A, 'e', 0xFFFFFFFF, 'd', 0xFF1A5A36);
    public static final String[] SLIME = {
            "......oooooo......",
            "....ooGGGGGGoo....",
            "...oGGllGGGGGGo...",
            "..oGGllGGGGGGGGo..",
            "..oGlGGGGGGGGGGo..",
            ".oGGGGeEGGGeEGGGo.",
            ".oGGGGEEGGGEEGGGo.",
            ".oGGGGGGGGGGGGGGo.",
            ".ogGGGGGGGGGGGGgo.",
            "oggGGGGddddGGGggo.",
            "oggggGGGGGGGGgggo.",
            "ogggggggggggggggo.",
            ".oddddddddddddddo.",
            "..oooooooooooooo.."
    };

    public static final Map<Character, Integer> BAT_PALETTE = PixelArt.palette(
            'o', 0xFF1E1240, 'V', 0xFF7048E8, 'v', 0xFF4B2DA8, 'l', 0xFFB197FC, 'Y', 0xFFFFD43B, 'y', 0xFFFF8A4C, 'W', 0xFFE8DEFF);
    public static final String[] BAT_UP = {
            "o....................o",
            "vo..................ov",
            "Vvo......o..o......ovV",
            "VVvo....oVooVo....ovVV",
            "VVVvo..oVVVVVVo..ovVVV",
            "lVVVvooVlVVVVVVoovVVVl",
            ".lVVVvVVYyVVyYVVvVVVl.",
            "..lVVVVVyYVVYyVVVVVl..",
            "...lVVoVVVVVVVVoVVl...",
            "....ll.oVVWWWVo.ll....",
            ".......ovVVVVvo.......",
            "........ovvvvo........",
            ".........oooo.........",
            "......................"
    };
    public static final String[] BAT_DOWN = {
            "......................",
            "......................",
            ".........o..o.........",
            "........oVooVo........",
            ".......oVVVVVVo.......",
            "......oVlVVVVVVo......",
            "....ooVVYyVVyYVVoo....",
            "..ovvVVVVyYVVYyVVVvvo.",
            ".ovVVVVVVVVVVVVVVVVVvo",
            "ovVVVVVVoVWWWVoVVVVVVv",
            "vVVVVll.ovVVVVvo.llVVV",
            "VVll.....ovvvvo....llV",
            "ll........oooo.......l",
            "......................"
    };

    public static final Map<Character, Integer> HEART_PALETTE = PixelArt.palette(
            'o', 0xFF2A1220, 'P', 0xFFE86A92, 'p', 0xFFB9446E, 'L', 0xFFFFD1E0);
    public static final Map<Character, Integer> HEART_EMPTY_PALETTE = PixelArt.palette(
            'o', 0xFF1A1420, 'P', 0xFF3A3045, 'p', 0xFF2E2638, 'L', 0xFF4A3F55);
    public static final String[] HEART = {
            ".oo...oo.",
            "oPLo.oPPo",
            "oPLPoPPPo",
            "oPPPPPPPo",
            "oPPPPPPpo",
            ".oPPPPpo.",
            "..oPPpo..",
            "...opo...",
            "....o...."
    };
}
