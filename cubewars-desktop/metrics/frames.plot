set encoding iso_8859_1
set terminal png

set title "Cubewars framerate" 

set xlabel "time"
set ylabel "fps"

set yrange [0:70]

set output "frames.png"
plot "frames.dat" title "Framerate" with linespoints smooth csplines
