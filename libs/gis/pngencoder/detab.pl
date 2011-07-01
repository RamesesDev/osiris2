#!/usr/bin/perl

use Text::Tabs;

foreach $item (@ARGV)
{
	print $item, "\n";
	open INFILE, $item or die("Cannot open $item\n");
	open OUTFILE, ">temptab";
	$tabstop = 4;
	while (<INFILE>)
	{
		print OUTFILE expand($_);
	}
	close INFILE;
	close OUTFILE;
	unlink $item; 
	rename "temptab", $item;
	print "$item processed\n";
}

