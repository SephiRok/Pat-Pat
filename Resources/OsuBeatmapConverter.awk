# Converts .osu beatmaps to PatPat .xml beatmaps.
BEGIN { 
	FS=":"
	print "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
	print "<map>"
}
{
if ($1 ~ /OverallDifficulty/) {
	speed = int(0.5 + $2 / 2)
} else if ($1 ~ /ApproachRate/) {
	speed = int(0.5 + $2 / 2)
} else if ($1 ~ /SliderMultiplier/) {
	sliderMultiplier = $2
	FS=","
} else if ($1 ~ /TimingPoints/) {
	print "\t<speed>"speed"</speed>"
	timingPoints = 1
} else if ($1 ~ /HitObjects/) {
	hitObjects = 1
} else if (timingPoints) {
	# offset, bpm^(-1)*60000, ?, ?, ?, volume, kiai
	offset = $1
	bps = 1 / $2 * 1000
	timingPoints = 0
} else if (hitObjects) {
	# $4 = 1 => normal beat, $4 = 5 => new beat color, $4 = 2/6 => slider.
	lane = int(3 * rand())
	beatTime = $3
	if (($4 == "1") || ($4 == "5")) {
		print "\t<beat time=\""beatTime"\" lane=\""lane"\" />"
	} else if (($4 == "2") || ($4 == "6")) {
		print "\t<beat time=\""beatTime"\" lane=\""lane"\" />"
		sliderRepeats = $7
		sliderLength = $8 / sliderMultiplier * bps
		# 0.5 is for rounding.
		print "\t<beat time=\""int(0.5 + beatTime + sliderRepeats * sliderLength)"\" lane=\""lane"\" />"
	}
}
}
END {
	print "</map>"
}