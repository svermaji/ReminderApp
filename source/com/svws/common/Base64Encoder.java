package com.svws.common;

public class Base64Encoder
{

    private static final char [ ] alphabet = {
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', // 0 to 7
      'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', // 8 to 15
      'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', // 16 to 23
      'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', // 24 to 31
      'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', // 32 to 39
      'o', 'p', 'q', 'r', 's', 't', 'u', 'v', // 40 to 47
      'w', 'x', 'y', 'z', '0', '1', '2', '3', // 48 to 55
      '4', '5', '6', '7', '8', '9', '+', '/' }; // 56 to 63


    public static String encode ( String s ) {
      return encode ( s.getBytes ( ) );
    }

    public static String encode ( byte [ ] octetString ) {
      int bits24;
      int bits6;

      char [ ] out
        = new char [ ( ( octetString.length - 1 ) / 3 + 1 ) * 4 ];

      int outIndex = 0;
      int i = 0;

      while ( ( i + 3 ) <= octetString.length ) {
        // store the octets
        bits24 = ( octetString [ i++ ] & 0xFF ) << 16;
        bits24 |= ( octetString [ i++ ] & 0xFF ) << 8;
        bits24 |= ( octetString [ i++ ] & 0xFF ) << 0;

        bits6 = ( bits24 & 0x00FC0000 ) >> 18;
        out [ outIndex++ ] = alphabet [ bits6 ];
        bits6 = ( bits24 & 0x0003F000 ) >> 12;
        out [ outIndex++ ] = alphabet [ bits6 ];
        bits6 = ( bits24 & 0x00000FC0 ) >> 6;
        out [ outIndex++ ] = alphabet [ bits6 ];
        bits6 = ( bits24 & 0x0000003F );
        out [ outIndex++ ] = alphabet [ bits6 ];
      }

      if ( octetString.length - i == 2 ) {
        // store the octets
        bits24 = ( octetString [ i ] & 0xFF ) << 16;
        bits24 |= ( octetString [ i + 1 ] & 0xFF ) << 8;

        bits6 = ( bits24 & 0x00FC0000 ) >> 18;
        out [ outIndex++ ] = alphabet [ bits6 ];
        bits6 = ( bits24 & 0x0003F000 ) >> 12;
        out [ outIndex++ ] = alphabet [ bits6 ];
        bits6 = ( bits24 & 0x00000FC0 ) >> 6;
        out [ outIndex++ ] = alphabet [ bits6 ];

        // padding
        out [ outIndex++ ] = '=';
      }
      else if ( octetString.length - i == 1 ) {
        // store the octets
        bits24 = ( octetString [ i ] & 0xFF ) << 16;

        bits6 = ( bits24 & 0x00FC0000 ) >> 18;
        out [ outIndex++ ] = alphabet [ bits6 ];
        bits6 = ( bits24 & 0x0003F000 ) >> 12;
        out [ outIndex++ ] = alphabet [ bits6 ];

        // padding
        out [ outIndex++ ] = '=';
        out [ outIndex++ ] = '=';
      }

      return new String ( out );
    }

    static public String decode(String data) {
        return new String(decode(data.toCharArray()));
    }

    static public byte[] decode(char[] data)
    {
        int tempLen = data.length; // start with everything we've got
        for( int ix=0; ix<data.length; ix++ )
        {
        int value = codes[ data[ix] & 0xFF ]; // ignore
        if( (value < 0) && (data[ix] != 61) ) // 61 is the '=' symbol (a padding null byte)
        {
            --tempLen; // aha, found some useless stuff to ignore!
        }
        }
        int len = ((tempLen + 3) / 4) * 3; // calculate length based on what remains!
        if( tempLen>0 && data[tempLen-1] == '=') --len;
        if( tempLen>1 && data[tempLen-2] == '=') --len;
        byte[] out = new byte[len];



        int shift = 0; // # of excess bits stored in accum
        int accum = 0; // excess bits
        int index = 0;

        // we now go through the entire array (NOT using the 'tempLen' value)
        for (int ix=0; ix<data.length; ix++)
        {
        int value = codes[ data[ix] & 0xFF ]; // ignore high byte of char
        if ( value >= 0 ) // skip over non-code
        {
            accum <<= 6; // bits shift up by 6 each time thru
            shift += 6; // loop, with new bits being put in
            accum |= value; // at the bottom.
            if ( shift >= 8 ) // whenever there are 8 or more shifted in,
            {
            shift -= 8; // write them out (from the top, leaving any
            out[index++] = // excess at the bottom for next iteration.
                (byte) ((accum >> shift) & 0xff);
            }
        }
        }

        // if there is STILL something wrong we just have to throw up now!
        if( index != out.length)
        {
        throw new Error("Miscalculated data length (wrote " + index + " instead of " + out.length + ")");
        }

        return out;
    }

    static private byte[] codes = new byte[256];
    static {
        for (int i=0; i<256; i++) codes[i] = -1;
        for (int i = 'A'; i <= 'Z'; i++) codes[i] = (byte)( i - 'A');
        for (int i = 'a'; i <= 'z'; i++) codes[i] = (byte)(26 + i - 'a');
        for (int i = '0'; i <= '9'; i++) codes[i] = (byte)(52 + i - '0');
        codes['+'] = 62;
        codes['/'] = 63;
    }

    public static void main(String [] args)
    {
        String opr="", enc="", dec="", ansenc="", ansdec="";
        if (args.length < 2)
        {
            System.out.println ("\n\tUsage: operator encode/decode");
            System.out.println ("\t\tExample: e encodeWord");
            System.out.println ("\t\tExample: d decodeWord");
            System.out.println ("\t\tExample: ed encodeWord decodeWord\n");
        }
        else
        {
            opr = args[0];
            System.out.println("");
            if (opr.equalsIgnoreCase("e"))
            {
                enc = args[1];
                ansenc = encode(enc);
                System.out.println("Value of " + enc + " after encoding value is: " + ansenc);
            }
            if (opr.equalsIgnoreCase("d"))
            {
                dec = args[1];
                ansdec = decode(dec);
                System.out.println("Value of " + dec + " after decoding value is: " + ansdec);
            }
            if (opr.equalsIgnoreCase("ed"))
            {
                enc = args[1];
                dec = args[2];
                ansdec = decode(dec);
                System.out.println("Value of " + dec + " after decoding value is: " + ansdec);
                ansenc = encode(enc);
                System.out.println("Value of " + enc + " after encoding value is: " + ansenc);
            }
            System.out.println("");
        }
    }
}
