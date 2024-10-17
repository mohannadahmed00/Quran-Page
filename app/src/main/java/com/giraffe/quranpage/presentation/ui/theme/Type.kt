package com.giraffe.quranpage.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.giraffe.quranpage.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
val uthmanicHafs = FontFamily(Font(R.font.uthmanic_hafs))

val fontFamilies = listOf(
    FontFamily(Font(R.font.qcf2001)),
    FontFamily(Font(R.font.qcf2002)),
    FontFamily(Font(R.font.qcf2003)),
    FontFamily(Font(R.font.qcf2004)),
    FontFamily(Font(R.font.qcf2005)),
    FontFamily(Font(R.font.qcf2006)),
    FontFamily(Font(R.font.qcf2007)),
    FontFamily(Font(R.font.qcf2008)),
    FontFamily(Font(R.font.qcf2009)),
    FontFamily(Font(R.font.qcf2010)),
    FontFamily(Font(R.font.qcf2011)),
    FontFamily(Font(R.font.qcf2012)),
    FontFamily(Font(R.font.qcf2013)),
    FontFamily(Font(R.font.qcf2014)),
    FontFamily(Font(R.font.qcf2015)),
    FontFamily(Font(R.font.qcf2016)),
    FontFamily(Font(R.font.qcf2017)),
    FontFamily(Font(R.font.qcf2018)),
    FontFamily(Font(R.font.qcf2019)),
    FontFamily(Font(R.font.qcf2020)),
    FontFamily(Font(R.font.qcf2021)),
    FontFamily(Font(R.font.qcf2022)),
    FontFamily(Font(R.font.qcf2023)),
    FontFamily(Font(R.font.qcf2024)),
    FontFamily(Font(R.font.qcf2025)),
    FontFamily(Font(R.font.qcf2026)),
    FontFamily(Font(R.font.qcf2027)),
    FontFamily(Font(R.font.qcf2028)),
    FontFamily(Font(R.font.qcf2029)),
    FontFamily(Font(R.font.qcf2030)),
    FontFamily(Font(R.font.qcf2031)),
    FontFamily(Font(R.font.qcf2032)),
    FontFamily(Font(R.font.qcf2033)),
    FontFamily(Font(R.font.qcf2034)),
    FontFamily(Font(R.font.qcf2035)),
    FontFamily(Font(R.font.qcf2036)),
    FontFamily(Font(R.font.qcf2037)),
    FontFamily(Font(R.font.qcf2038)),
    FontFamily(Font(R.font.qcf2039)),
    FontFamily(Font(R.font.qcf2040)),
    FontFamily(Font(R.font.qcf2041)),
    FontFamily(Font(R.font.qcf2042)),
    FontFamily(Font(R.font.qcf2043)),
    FontFamily(Font(R.font.qcf2044)),
    FontFamily(Font(R.font.qcf2045)),
    FontFamily(Font(R.font.qcf2046)),
    FontFamily(Font(R.font.qcf2047)),
    FontFamily(Font(R.font.qcf2048)),
    FontFamily(Font(R.font.qcf2049)),
    FontFamily(Font(R.font.qcf2050)),
    FontFamily(Font(R.font.qcf2051)),
    FontFamily(Font(R.font.qcf2052)),
    FontFamily(Font(R.font.qcf2053)),
    FontFamily(Font(R.font.qcf2054)),
    FontFamily(Font(R.font.qcf2055)),
    FontFamily(Font(R.font.qcf2056)),
    FontFamily(Font(R.font.qcf2057)),
    FontFamily(Font(R.font.qcf2058)),
    FontFamily(Font(R.font.qcf2059)),
    FontFamily(Font(R.font.qcf2060)),
    FontFamily(Font(R.font.qcf2061)),
    FontFamily(Font(R.font.qcf2062)),
    FontFamily(Font(R.font.qcf2063)),
    FontFamily(Font(R.font.qcf2064)),
    FontFamily(Font(R.font.qcf2065)),
    FontFamily(Font(R.font.qcf2066)),
    FontFamily(Font(R.font.qcf2067)),
    FontFamily(Font(R.font.qcf2068)),
    FontFamily(Font(R.font.qcf2069)),
    FontFamily(Font(R.font.qcf2070)),
    FontFamily(Font(R.font.qcf2071)),
    FontFamily(Font(R.font.qcf2072)),
    FontFamily(Font(R.font.qcf2073)),
    FontFamily(Font(R.font.qcf2074)),
    FontFamily(Font(R.font.qcf2075)),
    FontFamily(Font(R.font.qcf2076)),
    FontFamily(Font(R.font.qcf2077)),
    FontFamily(Font(R.font.qcf2078)),
    FontFamily(Font(R.font.qcf2079)),
    FontFamily(Font(R.font.qcf2080)),
    FontFamily(Font(R.font.qcf2081)),
    FontFamily(Font(R.font.qcf2082)),
    FontFamily(Font(R.font.qcf2083)),
    FontFamily(Font(R.font.qcf2084)),
    FontFamily(Font(R.font.qcf2085)),
    FontFamily(Font(R.font.qcf2086)),
    FontFamily(Font(R.font.qcf2087)),
    FontFamily(Font(R.font.qcf2088)),
    FontFamily(Font(R.font.qcf2089)),
    FontFamily(Font(R.font.qcf2090)),
    FontFamily(Font(R.font.qcf2091)),
    FontFamily(Font(R.font.qcf2092)),
    FontFamily(Font(R.font.qcf2093)),
    FontFamily(Font(R.font.qcf2094)),
    FontFamily(Font(R.font.qcf2095)),
    FontFamily(Font(R.font.qcf2096)),
    FontFamily(Font(R.font.qcf2097)),
    FontFamily(Font(R.font.qcf2098)),
    FontFamily(Font(R.font.qcf2099)),
    FontFamily(Font(R.font.qcf2100)),
    FontFamily(Font(R.font.qcf2101)),
    FontFamily(Font(R.font.qcf2102)),
    FontFamily(Font(R.font.qcf2103)),
    FontFamily(Font(R.font.qcf2104)),
    FontFamily(Font(R.font.qcf2105)),
    FontFamily(Font(R.font.qcf2106)),
    FontFamily(Font(R.font.qcf2107)),
    FontFamily(Font(R.font.qcf2108)),
    FontFamily(Font(R.font.qcf2109)),
    FontFamily(Font(R.font.qcf2110)),
    FontFamily(Font(R.font.qcf2111)),
    FontFamily(Font(R.font.qcf2112)),
    FontFamily(Font(R.font.qcf2113)),
    FontFamily(Font(R.font.qcf2114)),
    FontFamily(Font(R.font.qcf2115)),
    FontFamily(Font(R.font.qcf2116)),
    FontFamily(Font(R.font.qcf2117)),
    FontFamily(Font(R.font.qcf2118)),
    FontFamily(Font(R.font.qcf2119)),
    FontFamily(Font(R.font.qcf2120)),
    FontFamily(Font(R.font.qcf2121)),
    FontFamily(Font(R.font.qcf2122)),
    FontFamily(Font(R.font.qcf2123)),
    FontFamily(Font(R.font.qcf2124)),
    FontFamily(Font(R.font.qcf2125)),
    FontFamily(Font(R.font.qcf2126)),
    FontFamily(Font(R.font.qcf2127)),
    FontFamily(Font(R.font.qcf2128)),
    FontFamily(Font(R.font.qcf2129)),
    FontFamily(Font(R.font.qcf2130)),
    FontFamily(Font(R.font.qcf2131)),
    FontFamily(Font(R.font.qcf2132)),
    FontFamily(Font(R.font.qcf2133)),
    FontFamily(Font(R.font.qcf2134)),
    FontFamily(Font(R.font.qcf2135)),
    FontFamily(Font(R.font.qcf2136)),
    FontFamily(Font(R.font.qcf2137)),
    FontFamily(Font(R.font.qcf2138)),
    FontFamily(Font(R.font.qcf2139)),
    FontFamily(Font(R.font.qcf2140)),
    FontFamily(Font(R.font.qcf2141)),
    FontFamily(Font(R.font.qcf2142)),
    FontFamily(Font(R.font.qcf2143)),
    FontFamily(Font(R.font.qcf2144)),
    FontFamily(Font(R.font.qcf2145)),
    FontFamily(Font(R.font.qcf2146)),
    FontFamily(Font(R.font.qcf2147)),
    FontFamily(Font(R.font.qcf2148)),
    FontFamily(Font(R.font.qcf2149)),
    FontFamily(Font(R.font.qcf2150)),
    FontFamily(Font(R.font.qcf2151)),
    FontFamily(Font(R.font.qcf2152)),
    FontFamily(Font(R.font.qcf2153)),
    FontFamily(Font(R.font.qcf2154)),
    FontFamily(Font(R.font.qcf2155)),
    FontFamily(Font(R.font.qcf2156)),
    FontFamily(Font(R.font.qcf2157)),
    FontFamily(Font(R.font.qcf2158)),
    FontFamily(Font(R.font.qcf2159)),
    FontFamily(Font(R.font.qcf2160)),
    FontFamily(Font(R.font.qcf2161)),
    FontFamily(Font(R.font.qcf2162)),
    FontFamily(Font(R.font.qcf2163)),
    FontFamily(Font(R.font.qcf2164)),
    FontFamily(Font(R.font.qcf2165)),
    FontFamily(Font(R.font.qcf2166)),
    FontFamily(Font(R.font.qcf2167)),
    FontFamily(Font(R.font.qcf2168)),
    FontFamily(Font(R.font.qcf2169)),
    FontFamily(Font(R.font.qcf2170)),
    FontFamily(Font(R.font.qcf2171)),
    FontFamily(Font(R.font.qcf2172)),
    FontFamily(Font(R.font.qcf2173)),
    FontFamily(Font(R.font.qcf2174)),
    FontFamily(Font(R.font.qcf2175)),
    FontFamily(Font(R.font.qcf2176)),
    FontFamily(Font(R.font.qcf2177)),
    FontFamily(Font(R.font.qcf2178)),
    FontFamily(Font(R.font.qcf2179)),
    FontFamily(Font(R.font.qcf2180)),
    FontFamily(Font(R.font.qcf2181)),
    FontFamily(Font(R.font.qcf2182)),
    FontFamily(Font(R.font.qcf2183)),
    FontFamily(Font(R.font.qcf2184)),
    FontFamily(Font(R.font.qcf2185)),
    FontFamily(Font(R.font.qcf2186)),
    FontFamily(Font(R.font.qcf2187)),
    FontFamily(Font(R.font.qcf2188)),
    FontFamily(Font(R.font.qcf2189)),
    FontFamily(Font(R.font.qcf2190)),
    FontFamily(Font(R.font.qcf2191)),
    FontFamily(Font(R.font.qcf2192)),
    FontFamily(Font(R.font.qcf2193)),
    FontFamily(Font(R.font.qcf2194)),
    FontFamily(Font(R.font.qcf2195)),
    FontFamily(Font(R.font.qcf2196)),
    FontFamily(Font(R.font.qcf2197)),
    FontFamily(Font(R.font.qcf2198)),
    FontFamily(Font(R.font.qcf2199)),
    FontFamily(Font(R.font.qcf2200)),
    FontFamily(Font(R.font.qcf2201)),
    FontFamily(Font(R.font.qcf2202)),
    FontFamily(Font(R.font.qcf2203)),
    FontFamily(Font(R.font.qcf2204)),
    FontFamily(Font(R.font.qcf2205)),
    FontFamily(Font(R.font.qcf2206)),
    FontFamily(Font(R.font.qcf2207)),
    FontFamily(Font(R.font.qcf2208)),
    FontFamily(Font(R.font.qcf2209)),
    FontFamily(Font(R.font.qcf2210)),
    FontFamily(Font(R.font.qcf2211)),
    FontFamily(Font(R.font.qcf2212)),
    FontFamily(Font(R.font.qcf2213)),
    FontFamily(Font(R.font.qcf2214)),
    FontFamily(Font(R.font.qcf2215)),
    FontFamily(Font(R.font.qcf2216)),
    FontFamily(Font(R.font.qcf2217)),
    FontFamily(Font(R.font.qcf2218)),
    FontFamily(Font(R.font.qcf2219)),
    FontFamily(Font(R.font.qcf2220)),
    FontFamily(Font(R.font.qcf2221)),
    FontFamily(Font(R.font.qcf2222)),
    FontFamily(Font(R.font.qcf2223)),
    FontFamily(Font(R.font.qcf2224)),
    FontFamily(Font(R.font.qcf2225)),
    FontFamily(Font(R.font.qcf2226)),
    FontFamily(Font(R.font.qcf2227)),
    FontFamily(Font(R.font.qcf2228)),
    FontFamily(Font(R.font.qcf2229)),
    FontFamily(Font(R.font.qcf2230)),
    FontFamily(Font(R.font.qcf2231)),
    FontFamily(Font(R.font.qcf2232)),
    FontFamily(Font(R.font.qcf2233)),
    FontFamily(Font(R.font.qcf2234)),
    FontFamily(Font(R.font.qcf2235)),
    FontFamily(Font(R.font.qcf2236)),
    FontFamily(Font(R.font.qcf2237)),
    FontFamily(Font(R.font.qcf2238)),
    FontFamily(Font(R.font.qcf2239)),
    FontFamily(Font(R.font.qcf2240)),
    FontFamily(Font(R.font.qcf2241)),
    FontFamily(Font(R.font.qcf2242)),
    FontFamily(Font(R.font.qcf2243)),
    FontFamily(Font(R.font.qcf2244)),
    FontFamily(Font(R.font.qcf2245)),
    FontFamily(Font(R.font.qcf2246)),
    FontFamily(Font(R.font.qcf2247)),
    FontFamily(Font(R.font.qcf2248)),
    FontFamily(Font(R.font.qcf2249)),
    FontFamily(Font(R.font.qcf2250)),
    FontFamily(Font(R.font.qcf2251)),
    FontFamily(Font(R.font.qcf2252)),
    FontFamily(Font(R.font.qcf2253)),
    FontFamily(Font(R.font.qcf2254)),
    FontFamily(Font(R.font.qcf2255)),
    FontFamily(Font(R.font.qcf2256)),
    FontFamily(Font(R.font.qcf2257)),
    FontFamily(Font(R.font.qcf2258)),
    FontFamily(Font(R.font.qcf2259)),
    FontFamily(Font(R.font.qcf2260)),
    FontFamily(Font(R.font.qcf2261)),
    FontFamily(Font(R.font.qcf2262)),
    FontFamily(Font(R.font.qcf2263)),
    FontFamily(Font(R.font.qcf2264)),
    FontFamily(Font(R.font.qcf2265)),
    FontFamily(Font(R.font.qcf2266)),
    FontFamily(Font(R.font.qcf2267)),
    FontFamily(Font(R.font.qcf2268)),
    FontFamily(Font(R.font.qcf2269)),
    FontFamily(Font(R.font.qcf2270)),
    FontFamily(Font(R.font.qcf2271)),
    FontFamily(Font(R.font.qcf2272)),
    FontFamily(Font(R.font.qcf2273)),
    FontFamily(Font(R.font.qcf2274)),
    FontFamily(Font(R.font.qcf2275)),
    FontFamily(Font(R.font.qcf2276)),
    FontFamily(Font(R.font.qcf2277)),
    FontFamily(Font(R.font.qcf2278)),
    FontFamily(Font(R.font.qcf2279)),
    FontFamily(Font(R.font.qcf2280)),
    FontFamily(Font(R.font.qcf2281)),
    FontFamily(Font(R.font.qcf2282)),
    FontFamily(Font(R.font.qcf2283)),
    FontFamily(Font(R.font.qcf2284)),
    FontFamily(Font(R.font.qcf2285)),
    FontFamily(Font(R.font.qcf2286)),
    FontFamily(Font(R.font.qcf2287)),
    FontFamily(Font(R.font.qcf2288)),
    FontFamily(Font(R.font.qcf2289)),
    FontFamily(Font(R.font.qcf2290)),
    FontFamily(Font(R.font.qcf2291)),
    FontFamily(Font(R.font.qcf2292)),
    FontFamily(Font(R.font.qcf2293)),
    FontFamily(Font(R.font.qcf2294)),
    FontFamily(Font(R.font.qcf2295)),
    FontFamily(Font(R.font.qcf2296)),
    FontFamily(Font(R.font.qcf2297)),
    FontFamily(Font(R.font.qcf2298)),
    FontFamily(Font(R.font.qcf2299)),
    FontFamily(Font(R.font.qcf2300)),
    FontFamily(Font(R.font.qcf2301)),
    FontFamily(Font(R.font.qcf2302)),
    FontFamily(Font(R.font.qcf2303)),
    FontFamily(Font(R.font.qcf2304)),
    FontFamily(Font(R.font.qcf2305)),
    FontFamily(Font(R.font.qcf2306)),
    FontFamily(Font(R.font.qcf2307)),
    FontFamily(Font(R.font.qcf2308)),
    FontFamily(Font(R.font.qcf2309)),
    FontFamily(Font(R.font.qcf2310)),
    FontFamily(Font(R.font.qcf2311)),
    FontFamily(Font(R.font.qcf2312)),
    FontFamily(Font(R.font.qcf2313)),
    FontFamily(Font(R.font.qcf2314)),
    FontFamily(Font(R.font.qcf2315)),
    FontFamily(Font(R.font.qcf2316)),
    FontFamily(Font(R.font.qcf2317)),
    FontFamily(Font(R.font.qcf2318)),
    FontFamily(Font(R.font.qcf2319)),
    FontFamily(Font(R.font.qcf2320)),
    FontFamily(Font(R.font.qcf2321)),
    FontFamily(Font(R.font.qcf2322)),
    FontFamily(Font(R.font.qcf2323)),
    FontFamily(Font(R.font.qcf2324)),
    FontFamily(Font(R.font.qcf2325)),
    FontFamily(Font(R.font.qcf2326)),
    FontFamily(Font(R.font.qcf2327)),
    FontFamily(Font(R.font.qcf2328)),
    FontFamily(Font(R.font.qcf2329)),
    FontFamily(Font(R.font.qcf2330)),
    FontFamily(Font(R.font.qcf2331)),
    FontFamily(Font(R.font.qcf2332)),
    FontFamily(Font(R.font.qcf2333)),
    FontFamily(Font(R.font.qcf2334)),
    FontFamily(Font(R.font.qcf2335)),
    FontFamily(Font(R.font.qcf2336)),
    FontFamily(Font(R.font.qcf2337)),
    FontFamily(Font(R.font.qcf2338)),
    FontFamily(Font(R.font.qcf2339)),
    FontFamily(Font(R.font.qcf2340)),
    FontFamily(Font(R.font.qcf2341)),
    FontFamily(Font(R.font.qcf2342)),
    FontFamily(Font(R.font.qcf2343)),
    FontFamily(Font(R.font.qcf2344)),
    FontFamily(Font(R.font.qcf2345)),
    FontFamily(Font(R.font.qcf2346)),
    FontFamily(Font(R.font.qcf2347)),
    FontFamily(Font(R.font.qcf2348)),
    FontFamily(Font(R.font.qcf2349)),
    FontFamily(Font(R.font.qcf2350)),
    FontFamily(Font(R.font.qcf2351)),
    FontFamily(Font(R.font.qcf2352)),
    FontFamily(Font(R.font.qcf2353)),
    FontFamily(Font(R.font.qcf2354)),
    FontFamily(Font(R.font.qcf2355)),
    FontFamily(Font(R.font.qcf2356)),
    FontFamily(Font(R.font.qcf2357)),
    FontFamily(Font(R.font.qcf2358)),
    FontFamily(Font(R.font.qcf2359)),
    FontFamily(Font(R.font.qcf2360)),
    FontFamily(Font(R.font.qcf2361)),
    FontFamily(Font(R.font.qcf2362)),
    FontFamily(Font(R.font.qcf2363)),
    FontFamily(Font(R.font.qcf2364)),
    FontFamily(Font(R.font.qcf2365)),
    FontFamily(Font(R.font.qcf2366)),
    FontFamily(Font(R.font.qcf2367)),
    FontFamily(Font(R.font.qcf2368)),
    FontFamily(Font(R.font.qcf2369)),
    FontFamily(Font(R.font.qcf2370)),
    FontFamily(Font(R.font.qcf2371)),
    FontFamily(Font(R.font.qcf2372)),
    FontFamily(Font(R.font.qcf2373)),
    FontFamily(Font(R.font.qcf2374)),
    FontFamily(Font(R.font.qcf2375)),
    FontFamily(Font(R.font.qcf2376)),
    FontFamily(Font(R.font.qcf2377)),
    FontFamily(Font(R.font.qcf2378)),
    FontFamily(Font(R.font.qcf2379)),
    FontFamily(Font(R.font.qcf2380)),
    FontFamily(Font(R.font.qcf2381)),
    FontFamily(Font(R.font.qcf2382)),
    FontFamily(Font(R.font.qcf2383)),
    FontFamily(Font(R.font.qcf2384)),
    FontFamily(Font(R.font.qcf2385)),
    FontFamily(Font(R.font.qcf2386)),
    FontFamily(Font(R.font.qcf2387)),
    FontFamily(Font(R.font.qcf2388)),
    FontFamily(Font(R.font.qcf2389)),
    FontFamily(Font(R.font.qcf2390)),
    FontFamily(Font(R.font.qcf2391)),
    FontFamily(Font(R.font.qcf2392)),
    FontFamily(Font(R.font.qcf2393)),
    FontFamily(Font(R.font.qcf2394)),
    FontFamily(Font(R.font.qcf2395)),
    FontFamily(Font(R.font.qcf2396)),
    FontFamily(Font(R.font.qcf2397)),
    FontFamily(Font(R.font.qcf2398)),
    FontFamily(Font(R.font.qcf2399)),
    FontFamily(Font(R.font.qcf2400)),
    FontFamily(Font(R.font.qcf2401)),
    FontFamily(Font(R.font.qcf2402)),
    FontFamily(Font(R.font.qcf2403)),
    FontFamily(Font(R.font.qcf2404)),
    FontFamily(Font(R.font.qcf2405)),
    FontFamily(Font(R.font.qcf2406)),
    FontFamily(Font(R.font.qcf2407)),
    FontFamily(Font(R.font.qcf2408)),
    FontFamily(Font(R.font.qcf2409)),
    FontFamily(Font(R.font.qcf2410)),
    FontFamily(Font(R.font.qcf2411)),
    FontFamily(Font(R.font.qcf2412)),
    FontFamily(Font(R.font.qcf2413)),
    FontFamily(Font(R.font.qcf2414)),
    FontFamily(Font(R.font.qcf2415)),
    FontFamily(Font(R.font.qcf2416)),
    FontFamily(Font(R.font.qcf2417)),
    FontFamily(Font(R.font.qcf2418)),
    FontFamily(Font(R.font.qcf2419)),
    FontFamily(Font(R.font.qcf2420)),
    FontFamily(Font(R.font.qcf2421)),
    FontFamily(Font(R.font.qcf2422)),
    FontFamily(Font(R.font.qcf2423)),
    FontFamily(Font(R.font.qcf2424)),
    FontFamily(Font(R.font.qcf2425)),
    FontFamily(Font(R.font.qcf2426)),
    FontFamily(Font(R.font.qcf2427)),
    FontFamily(Font(R.font.qcf2428)),
    FontFamily(Font(R.font.qcf2429)),
    FontFamily(Font(R.font.qcf2430)),
    FontFamily(Font(R.font.qcf2431)),
    FontFamily(Font(R.font.qcf2432)),
    FontFamily(Font(R.font.qcf2433)),
    FontFamily(Font(R.font.qcf2434)),
    FontFamily(Font(R.font.qcf2435)),
    FontFamily(Font(R.font.qcf2436)),
    FontFamily(Font(R.font.qcf2437)),
    FontFamily(Font(R.font.qcf2438)),
    FontFamily(Font(R.font.qcf2439)),
    FontFamily(Font(R.font.qcf2440)),
    FontFamily(Font(R.font.qcf2441)),
    FontFamily(Font(R.font.qcf2442)),
    FontFamily(Font(R.font.qcf2443)),
    FontFamily(Font(R.font.qcf2444)),
    FontFamily(Font(R.font.qcf2445)),
    FontFamily(Font(R.font.qcf2446)),
    FontFamily(Font(R.font.qcf2447)),
    FontFamily(Font(R.font.qcf2448)),
    FontFamily(Font(R.font.qcf2449)),
    FontFamily(Font(R.font.qcf2450)),
    FontFamily(Font(R.font.qcf2451)),
    FontFamily(Font(R.font.qcf2452)),
    FontFamily(Font(R.font.qcf2453)),
    FontFamily(Font(R.font.qcf2454)),
    FontFamily(Font(R.font.qcf2455)),
    FontFamily(Font(R.font.qcf2456)),
    FontFamily(Font(R.font.qcf2457)),
    FontFamily(Font(R.font.qcf2458)),
    FontFamily(Font(R.font.qcf2459)),
    FontFamily(Font(R.font.qcf2460)),
    FontFamily(Font(R.font.qcf2461)),
    FontFamily(Font(R.font.qcf2462)),
    FontFamily(Font(R.font.qcf2463)),
    FontFamily(Font(R.font.qcf2464)),
    FontFamily(Font(R.font.qcf2465)),
    FontFamily(Font(R.font.qcf2466)),
    FontFamily(Font(R.font.qcf2467)),
    FontFamily(Font(R.font.qcf2468)),
    FontFamily(Font(R.font.qcf2469)),
    FontFamily(Font(R.font.qcf2470)),
    FontFamily(Font(R.font.qcf2471)),
    FontFamily(Font(R.font.qcf2472)),
    FontFamily(Font(R.font.qcf2473)),
    FontFamily(Font(R.font.qcf2474)),
    FontFamily(Font(R.font.qcf2475)),
    FontFamily(Font(R.font.qcf2476)),
    FontFamily(Font(R.font.qcf2477)),
    FontFamily(Font(R.font.qcf2478)),
    FontFamily(Font(R.font.qcf2479)),
    FontFamily(Font(R.font.qcf2480)),
    FontFamily(Font(R.font.qcf2481)),
    FontFamily(Font(R.font.qcf2482)),
    FontFamily(Font(R.font.qcf2483)),
    FontFamily(Font(R.font.qcf2484)),
    FontFamily(Font(R.font.qcf2485)),
    FontFamily(Font(R.font.qcf2486)),
    FontFamily(Font(R.font.qcf2487)),
    FontFamily(Font(R.font.qcf2488)),
    FontFamily(Font(R.font.qcf2489)),
    FontFamily(Font(R.font.qcf2490)),
    FontFamily(Font(R.font.qcf2491)),
    FontFamily(Font(R.font.qcf2492)),
    FontFamily(Font(R.font.qcf2493)),
    FontFamily(Font(R.font.qcf2494)),
    FontFamily(Font(R.font.qcf2495)),
    FontFamily(Font(R.font.qcf2496)),
    FontFamily(Font(R.font.qcf2497)),
    FontFamily(Font(R.font.qcf2498)),
    FontFamily(Font(R.font.qcf2499)),
    FontFamily(Font(R.font.qcf2500)),
    FontFamily(Font(R.font.qcf2501)),
    FontFamily(Font(R.font.qcf2502)),
    FontFamily(Font(R.font.qcf2503)),
    FontFamily(Font(R.font.qcf2504)),
    FontFamily(Font(R.font.qcf2505)),
    FontFamily(Font(R.font.qcf2506)),
    FontFamily(Font(R.font.qcf2507)),
    FontFamily(Font(R.font.qcf2508)),
    FontFamily(Font(R.font.qcf2509)),
    FontFamily(Font(R.font.qcf2510)),
    FontFamily(Font(R.font.qcf2511)),
    FontFamily(Font(R.font.qcf2512)),
    FontFamily(Font(R.font.qcf2513)),
    FontFamily(Font(R.font.qcf2514)),
    FontFamily(Font(R.font.qcf2515)),
    FontFamily(Font(R.font.qcf2516)),
    FontFamily(Font(R.font.qcf2517)),
    FontFamily(Font(R.font.qcf2518)),
    FontFamily(Font(R.font.qcf2519)),
    FontFamily(Font(R.font.qcf2520)),
    FontFamily(Font(R.font.qcf2521)),
    FontFamily(Font(R.font.qcf2522)),
    FontFamily(Font(R.font.qcf2523)),
    FontFamily(Font(R.font.qcf2524)),
    FontFamily(Font(R.font.qcf2525)),
    FontFamily(Font(R.font.qcf2526)),
    FontFamily(Font(R.font.qcf2527)),
    FontFamily(Font(R.font.qcf2528)),
    FontFamily(Font(R.font.qcf2529)),
    FontFamily(Font(R.font.qcf2530)),
    FontFamily(Font(R.font.qcf2531)),
    FontFamily(Font(R.font.qcf2532)),
    FontFamily(Font(R.font.qcf2533)),
    FontFamily(Font(R.font.qcf2534)),
    FontFamily(Font(R.font.qcf2535)),
    FontFamily(Font(R.font.qcf2536)),
    FontFamily(Font(R.font.qcf2537)),
    FontFamily(Font(R.font.qcf2538)),
    FontFamily(Font(R.font.qcf2539)),
    FontFamily(Font(R.font.qcf2540)),
    FontFamily(Font(R.font.qcf2541)),
    FontFamily(Font(R.font.qcf2542)),
    FontFamily(Font(R.font.qcf2543)),
    FontFamily(Font(R.font.qcf2544)),
    FontFamily(Font(R.font.qcf2545)),
    FontFamily(Font(R.font.qcf2546)),
    FontFamily(Font(R.font.qcf2547)),
    FontFamily(Font(R.font.qcf2548)),
    FontFamily(Font(R.font.qcf2549)),
    FontFamily(Font(R.font.qcf2550)),
    FontFamily(Font(R.font.qcf2551)),
    FontFamily(Font(R.font.qcf2552)),
    FontFamily(Font(R.font.qcf2553)),
    FontFamily(Font(R.font.qcf2554)),
    FontFamily(Font(R.font.qcf2555)),
    FontFamily(Font(R.font.qcf2556)),
    FontFamily(Font(R.font.qcf2557)),
    FontFamily(Font(R.font.qcf2558)),
    FontFamily(Font(R.font.qcf2559)),
    FontFamily(Font(R.font.qcf2560)),
    FontFamily(Font(R.font.qcf2561)),
    FontFamily(Font(R.font.qcf2562)),
    FontFamily(Font(R.font.qcf2563)),
    FontFamily(Font(R.font.qcf2564)),
    FontFamily(Font(R.font.qcf2565)),
    FontFamily(Font(R.font.qcf2566)),
    FontFamily(Font(R.font.qcf2567)),
    FontFamily(Font(R.font.qcf2568)),
    FontFamily(Font(R.font.qcf2569)),
    FontFamily(Font(R.font.qcf2570)),
    FontFamily(Font(R.font.qcf2571)),
    FontFamily(Font(R.font.qcf2572)),
    FontFamily(Font(R.font.qcf2573)),
    FontFamily(Font(R.font.qcf2574)),
    FontFamily(Font(R.font.qcf2575)),
    FontFamily(Font(R.font.qcf2576)),
    FontFamily(Font(R.font.qcf2577)),
    FontFamily(Font(R.font.qcf2578)),
    FontFamily(Font(R.font.qcf2579)),
    FontFamily(Font(R.font.qcf2580)),
    FontFamily(Font(R.font.qcf2581)),
    FontFamily(Font(R.font.qcf2582)),
    FontFamily(Font(R.font.qcf2583)),
    FontFamily(Font(R.font.qcf2584)),
    FontFamily(Font(R.font.qcf2585)),
    FontFamily(Font(R.font.qcf2586)),
    FontFamily(Font(R.font.qcf2587)),
    FontFamily(Font(R.font.qcf2588)),
    FontFamily(Font(R.font.qcf2589)),
    FontFamily(Font(R.font.qcf2590)),
    FontFamily(Font(R.font.qcf2591)),
    FontFamily(Font(R.font.qcf2592)),
    FontFamily(Font(R.font.qcf2593)),
    FontFamily(Font(R.font.qcf2594)),
    FontFamily(Font(R.font.qcf2595)),
    FontFamily(Font(R.font.qcf2596)),
    FontFamily(Font(R.font.qcf2597)),
    FontFamily(Font(R.font.qcf2598)),
    FontFamily(Font(R.font.qcf2599)),
    FontFamily(Font(R.font.qcf2600)),
    FontFamily(Font(R.font.qcf2601)),
    FontFamily(Font(R.font.qcf2602)),
    FontFamily(Font(R.font.qcf2603)),
    FontFamily(Font(R.font.qcf2604))
)
