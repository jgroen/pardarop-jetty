#!/bin/bash


for i in *.mp4; do
    fn=${i%.mp4}
    MP4Box -dash 200 -frag 20 -rap -profile onDemand -out $fn.mpd $i
    sleep 2    
done

exit
