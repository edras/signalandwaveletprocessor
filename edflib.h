/*
*****************************************************************************
*
* Copyright (c) 2009, 2010 Teunis van Beelen
* All rights reserved.
*
* email: teuniz@gmail.com
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*
* THIS SOFTWARE IS PROVIDED BY Teunis van Beelen ''AS IS'' AND ANY
* EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL Teunis van Beelen BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*
*****************************************************************************
*/



/* compile with options "-D_LARGEFILE64_SOURCE -D_LARGEFILE_SOURCE" */



#ifndef EDFLIB_INCLUDED
#define EDFLIB_INCLUDED


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>


#define EDFLIB_TIME_DIMENSION (10000000LL)
#define EDFLIB_MAXSIGNALS 256
#define EDFLIB_MAX_ANNOTATION_LEN 512

#define EDFSEEK_SET 0
#define EDFSEEK_CUR 1
#define EDFSEEK_END 2



/* the following defines are used in the member "filetype" of the edf_hdr_struct */
/* and as return value for the function edfopen_file_readonly() */
#define EDFLIB_FILETYPE_EDF                  0
#define EDFLIB_FILETYPE_EDFPLUS              1
#define EDFLIB_FILETYPE_BDF                  2
#define EDFLIB_FILETYPE_BDFPLUS              3
#define EDFLIB_MALLOC_ERROR                 -1
#define EDFLIB_NO_SUCH_FILE_OR_DIRECTORY    -2
#define EDFLIB_FILE_CONTAINS_FORMAT_ERRORS  -3
#define EDFLIB_MAXFILES_REACHED             -4
#define EDFLIB_FILE_READ_ERROR              -5
#define EDFLIB_FILE_ALREADY_OPENED          -6
#define EDFLIB_FILETYPE_ERROR               -7
#define EDFLIB_FILE_WRITE_ERROR             -8
#define EDFLIB_NUMBER_OF_SIGNALS_INVALID    -9
#define EDFLIB_FILE_IS_DISCONTINUOUS       -10
#define EDFLIB_INVALID_READ_ANNOTS_VALUE   -11

/* values for annotations */
#define EDFLIB_DO_NOT_READ_ANNOTATIONS 0
#define EDFLIB_READ_ANNOTATIONS        1
#define EDFLIB_READ_ALL_ANNOTATIONS    2

/* the following defines are possible errors returned by edfopen_file_writeonly() */
#define EDFLIB_NO_SIGNALS                  -20
#define EDFLIB_TOO_MANY_SIGNALS            -21
#define EDFLIB_NO_SAMPLES_IN_RECORD        -22
#define EDFLIB_DIGMIN_IS_DIGMAX            -23
#define EDFLIB_DIGMAX_LOWER_THAN_DIGMIN    -24
#define EDFLIB_PHYSMIN_IS_PHYSMAX          -25




#ifdef __cplusplus
extern "C" {
#endif






struct edf_param_struct{         /* this structure contains all the relevant EDF-signal parameters of one signal */
  char   label[17];              /* label (name) of the signal, null-terminated string */
  long long smp_in_file;         /* number of samples of this signal in the file */
  double phys_max;               /* physical maximum */
  double phys_min;               /* physical minimum */
  int    dig_max;                /* digital maximum */
  int    dig_min;                /* digital minimum */
  int    smp_in_datarecord;      /* number of samples of this signal in a datarecord */
  char   physdimension[9];       /* physical dimension (uV, bpm, mA, etc.), null-terminated string */
  char   prefilter[81];          /* null-terminated string */
  char   transducer[81];         /* null-terminated string */
      };


struct edf_annotation_struct{                           /* this structure is used for annotations */
        long long onset;                                /* onset time of the event, expressed in units of 100 nanoSeconds */
        char duration[16];                              /* duration time, this is a null-terminated ASCII text-string */
        char annotation[EDFLIB_MAX_ANNOTATION_LEN + 1]; /* description of the event in UTF-8, this is a null terminated string */
       };


struct edf_hdr_struct{                     /* this structure contains all the relevant EDF header info and will be filled when calling the function edf_open_file_readonly() */
  int       handle;                        /* a handle (identifier) used to distinguish the different files */
  int       filetype;                      /* 0: EDF, 1: EDFplus, 2: BDF, 3: BDFplus, a negative number means an error */
  int       edfsignals;                    /* number of EDF signals in the file, annotation channels are NOT included */
  long long file_duration;                 /* duration of the file expressed in units of 100 nanoSeconds */
  int       startdate_day;
  int       startdate_month;
  int       startdate_year;
  long long starttime_subsecond;                          /* starttime offset expressed in units of 100 nanoSeconds. Is always less than 10000000 (one second). Only used by EDFplus and BDFplus */
  int       starttime_second;
  int       starttime_minute;
  int       starttime_hour;
  char      patient[81];                                  /* null-terminated string, contains patientfield of header, is always empty when filetype is EDFPLUS or BDFPLUS */
  char      recording[81];                                /* null-terminated string, contains recordingfield of header, is always empty when filetype is EDFPLUS or BDFPLUS */
  char      patientcode[81];                              /* null-terminated string, is always empty when filetype is EDF or BDF */
  char      gender[16];                                   /* null-terminated string, is always empty when filetype is EDF or BDF */
  char      birthdate[16];                                /* null-terminated string, is always empty when filetype is EDF or BDF */
  char      patient_name[81];                             /* null-terminated string, is always empty when filetype is EDF or BDF */
  char      patient_additional[81];                       /* null-terminated string, is always empty when filetype is EDF or BDF */
  char      admincode[81];                                /* null-terminated string, is always empty when filetype is EDF or BDF */
  char      technician[81];                               /* null-terminated string, is always empty when filetype is EDF or BDF */
  char      equipment[81];                                /* null-terminated string, is always empty when filetype is EDF or BDF */
  char      recording_additional[81];                     /* null-terminated string, is always empty when filetype is EDF or BDF */
  long long datarecord_duration;                          /* duration of a datarecord expressed in units of 100 nanoSeconds */
  long long datarecords_in_file;                          /* number of datarecords in the file */
  long long annotations_in_file;                          /* number of annotations in the file */
  struct edf_param_struct signalparam[EDFLIB_MAXSIGNALS]; /* array of structs which contain the relevant signal parameters */
       };




int edflib_version(void);

/* Returns the version number of this library, multiplied by hundred. if version is "1.00" than it will return 100 */



/*****************  the following functions are used to read files **************************/



int edfopen_file_readonly(const char *path, struct edf_hdr_struct *edfhdr, int read_annotations);

/* opens an existing file for reading */
/* path is a null-terminated string containing the path to the file */
/* hdr is a pointer to an edf_hdr_struct, all fields in this struct will be overwritten */
/* the edf_hdr_struct will be filled with all the relevant header- and signalinfo/parameters */

/* read_annotations must have one of the following values:      */
/*   EDFLIB_DO_NOT_READ_ANNOTATIONS      annotations will not be read (this saves time when opening a very large EDFplus or BDFplus file */
/*   EDFLIB_READ_ANNOTATIONS             annotations will be read immediately, stops when an annotation has */
/*                                       been found which contains the description "Recording ends"         */
/*   EDFLIB_READ_ALL_ANNOTATIONS         all annotations will be read immediately                           */

/* returns 0 on success, in case of an error it returns -1 and an errorcode will be set in the member "filetype" of struct edf_hdr_struct */
/* This function is required if you want to read a file */



int edfclose_file(int handle);

/* closes and finalizes the file */
/* returns -1 in case of an error, 0 on success */
/* this function MUST be called when you are finished reading or writing */
/* This function is required after reading or writing. Failing to do so will cause */
/* unnessecary memory usage and in case of writing it will cause a corrupted and incomplete file */


int edfread_physical_samples(int handle, int edfsignal, int n, double *buf);

/* reads n samples from edfsignal, starting from the current sample position indicator, into buf (edfsignal starts at 0) */
/* the values are converted to their physical values e.g. microVolts, beats per minute, etc. */
/* bufsize should be equal to or bigger than sizeof(double[n]) */
/* the sample position indicator will be increased with the amount of samples read */
/* returns the amount of samples read (this can be less than n or zero!) */
/* or -1 in case of an error */


int edfread_digital_samples(int handle, int edfsignal, int n, int *buf);

/* reads n samples from edfsignal, starting from the current sample position indicator, into buf (edfsignal starts at 0) */
/* the values are the "raw" digital values */
/* bufsize should be equal to or bigger than sizeof(int[n]) */
/* the sample position indicator will be increased with the amount of samples read */
/* returns the amount of samples read (this can be less than n or zero!) */
/* or -1 in case of an error */


long long edfseek(int handle, int edfsignal, long long offset, int whence);

/* The edfseek() function sets the sample position indicator for the edfsignal pointed to by edfsignal. */
/* The new position, measured in samples, is obtained by adding offset samples to the position specified by whence. */
/* If whence is set to EDFSEEK_SET, EDFSEEK_CUR, or EDFSEEK_END, the offset is relative to the start of the file, */
/* the current position indicator, or end-of-file, respectively. */
/* Returns the current offset. Otherwise, -1 is returned. */
/* note that every signal has it's own independent sample position indicator and edfseek() affects only one of them */


long long edftell(int handle, int edfsignal);

/* The edftell() function obtains the current value of the sample position indicator for the edfsignal pointed to by edfsignal. */
/* Returns the current offset. Otherwise, -1 is returned */
/* note that every signal has it's own independent sample position indicator and edftell() affects only one of them */


void edfrewind(int handle, int edfsignal);

/* The edfrewind() function sets the sample position indicator for the edfsignal pointed to by edfsignal to the beginning of the file. */
/* It is equivalent to: (void) edfseek(int handle, int edfsignal, 0LL, EDFSEEK_SET) */
/* note that every signal has it's own independent sample position indicator and edfrewind() affects only one of them */


int edf_get_annotation(int handle, int n, struct edf_annotation_struct *annot);

/* Fills the edf_annotation_struct with the annotation n, returns 0 on success, otherwise -1 */
/* To obtain the number of annotations in a file, check edf_hdr_struct -> annotations_in_file */

/*
Notes:

Annotationsignals

EDFplus and BDFplus store the annotations in one or more signals (in order to be backwards compatibel with EDF and BDF).
The counting of the signals in the file starts at 0. Signals used for annotations are skipped by EDFlib.
This means that the annotationsignal(s) in the file are hided.
Use the function edf_get_annotation() to get the annotations.

So, when a file contains 5 signals and the third signal is used to store the annotations, the library will
report that there are only 4 signals in the file.
The library will "map" the signalnumbers as follows: 0->0, 1->1, 2->3, 3->4.
This way you don't need to worry about which signals are annotationsignals. The library will do it for you.

How the library stores time-values

To avoid rounding errors, the library stores some timevalues in variables of type long long int.
In order not to loose the subsecond precision, all timevalues have been multiplied by 10000000.
This will limit the timeresolution to 100 nanoSeconds. To calculate the amount of seconds, divide
the timevalue by 10000000 or use the macro EDFLIB_TIME_DIMENSION which is declared in edflib.h.
The following variables do use this when you open a file in read mode: "file_duration", "starttime_subsecond" and "onset".
*/


/*****************  the following functions are used to write files **************************/


int edfopen_file_writeonly(const char *path, int filetype, int number_of_signals);

/* opens an new file for writing. warning, an already existing file with the same name will be silently overwritten without advance warning!! */
/* path is a null-terminated string containing the path and name of the file */
/* filetype must be EDFLIB_FILETYPE_EDFPLUS or EDFLIB_FILETYPE_BDFPLUS */
/* returns a handle on success, you need this handle for the other functions */
/* in case of an error it returns a negative number corresponding to one of the following values: */
/* EDFLIB_MALLOC_ERROR                */
/* EDFLIB_NO_SUCH_FILE_OR_DIRECTORY   */
/* EDFLIB_MAXFILES_REACHED            */
/* EDFLIB_FILE_ALREADY_OPENED         */
/* EDFLIB_NUMBER_OF_SIGNALS_INVALID   */
/* This function is required if you want to write a file */


int edf_set_samplefrequency(int handle, int edfsignal, int samplefrequency);

/* Sets the samplefrequency of signal edfsignal. */
/* Returns 0 on success, otherwise -1 */
/* This function is required for every signal and can be called only after opening a */
/* file in writemode and before the first sample write action */


int edf_set_physical_maximum(int handle, int edfsignal, double phys_max);

/* Sets the maximum physical value of signal edfsignal. */
/* Returns 0 on success, otherwise -1 */
/* This function is required for every signal and can be called only after opening a */
/* file in writemode and before the first sample write action */


int edf_set_physical_minimum(int handle, int edfsignal, double phys_min);

/* Sets the minimum physical value of signal edfsignal. */
/* Usually this will be (-(phys_max)) */
/* Returns 0 on success, otherwise -1 */
/* This function is required for every signal and can be called only after opening a */
/* file in writemode and before the first sample write action */


int edf_set_digital_maximum(int handle, int edfsignal, int dig_max);

/* Sets the maximum digital value of signal edfsignal. Usually, the value 32767 is used for EDF+ and 8388607 for BDF+ */
/* Returns 0 on success, otherwise -1 */
/* This function is required for every signal and can be called only after opening a file in writemode */
/* and before the first sample write action */


int edf_set_digital_minimum(int handle, int edfsignal, int dig_min);

/* Sets the minimum digital value of signal edfsignal. Usually, the value -32768 is used for EDF+ and -8388608 for BDF+ */
/* Usually this will be (-(dig_max + 1)) */
/* Returns 0 on success, otherwise -1 */
/* This function is required for every signal and can be called only after opening a file in writemode */
/* and before the first sample write action */


int edf_set_label(int handle, int edfsignal, const char *label);

/* Sets the label (name) of signal edfsignal. ("FP1", "SaO2", etc.) */
/* label is a pointer to a NULL-terminated ASCII-string containing the label (name) of the signal edfsignal */
/* Returns 0 on success, otherwise -1 */
/* This function is recommanded for every signal when you want to write a file */
/* and can be called only after opening a file in writemode and before the first sample write action */


int edf_set_prefilter(int handle, int edfsignal, const char *prefilter);

/* Sets the prefilter of signal edfsignal ("HP:0.1Hz", "LP:75Hz N:50Hz", etc.). */
/* prefilter is a pointer to a NULL-terminated ASCII-string containing the prefilter text of the signal edfsignal */
/* Returns 0 on success, otherwise -1 */
/* This function is optional and can be called only after opening a file in writemode and before */
/* the first sample write action */


int edf_set_transducer(int handle, int edfsignal, const char *transducer);

/* Sets the transducer of signal edfsignal ("AgAgCl cup electrodes", etc.). */
/* transducer is a pointer to a NULL-terminated ASCII-string containing the transducer text of the signal edfsignal */
/* Returns 0 on success, otherwise -1 */
/* This function is optional and can be called only after opening a file in writemode and before */
/* the first sample write action */


int edf_set_physical_dimension(int handle, int edfsignal, const char *phys_dim);

/* Sets the physical dimension of signal edfsignal. ("uV", "BPM", "mA", "Degr.", etc.) */
/* phys_dim is a pointer to a NULL-terminated ASCII-string containing the physical dimension of the signal edfsignal */
/* Returns 0 on success, otherwise -1 */
/* This function is recommanded for every signal when you want to write a file */
/* and can be called only after opening a file in writemode and before the first sample write action */


int edf_set_startdatetime(int handle, int startdate_year, int startdate_month, int startdate_day,
                                      int starttime_hour, int starttime_minute, int starttime_second);

/* Sets the startdate and starttime. */
/* year: 1970 - 3000, month: 1 - 12, day: 1 - 31 */
/* hour: 0 - 23, minute: 0 - 59, second: 0 - 59 */
/* If not called, the library will use the system date and time at runtime */
/* Returns 0 on success, otherwise -1 */
/* This function is optional and can be called only after opening a file in writemode */
/* and before the first sample write action */


int edf_set_patientname(int handle, const char *patientname);

/* Sets the patientname. patientname is a pointer to a null-terminated ASCII-string. */
/* Returns 0 on success, otherwise -1 */
/* This function is optional and can be called only after opening a file in writemode */
/* and before the first sample write action */


int edf_set_patientcode(int handle, const char *patientcode);

/* Sets the patientcode. patientcode is a pointer to a null-terminated ASCII-string. */
/* Returns 0 on success, otherwise -1 */
/* This function is optional and can be called only after opening a file in writemode */
/* and before the first sample write action */


int edf_set_gender(int handle, int gender);

/* Sets the gender. 1 is male, 0 is female. */
/* Returns 0 on success, otherwise -1 */
/* This function is optional and can be called only after opening a file in writemode */
/* and before the first sample write action */



int edf_set_birthdate(int handle, int birthdate_year, int birthdate_month, int birthdate_day);

/* Sets the birthdate. */
/* year: 1800 - 3000, month: 1 - 12, day: 1 - 31 */
/* This function is optional */
/* Returns 0 on success, otherwise -1 */
/* This function is optional and can be called only after opening a file in writemode */
/* and before the first sample write action */


int edf_set_patient_additional(int handle, const char *patient_additional);

/* Sets the additional patientinfo. patient_additional is a pointer to a null-terminated ASCII-string. */
/* Returns 0 on success, otherwise -1 */
/* This function is optional and can be called only after opening a file in writemode */
/* and before the first sample write action */


int edf_set_admincode(int handle, const char *admincode);

/* Sets the admincode. admincode is a pointer to a null-terminated ASCII-string. */
/* Returns 0 on success, otherwise -1 */
/* This function is optional and can be called only after opening a file in writemode */
/* and before the first sample write action */


int edf_set_technician(int handle, const char *technician);

/* Sets the technicians name. technician is a pointer to a null-terminated ASCII-string. */
/* Returns 0 on success, otherwise -1 */
/* This function is optional and can be called only after opening a file in writemode */
/* and before the first sample write action */


int edf_set_equipment(int handle, const char *equipment);

/* Sets the name of the equipment used during the aquisition. equipment is a pointer to a null-terminated ASCII-string. */
/* Returns 0 on success, otherwise -1 */
/* This function is optional and can be called only after opening a file in writemode */
/* and before the first sample write action */


int edf_set_recording_additional(int handle, const char *recording_additional);

/* Sets the additional recordinginfo. recording_additional is a pointer to a null-terminated ASCII-string. */
/* Returns 0 on success, otherwise -1 */
/* This function is optional and can be called only after opening a file in writemode */
/* and before the first sample write action */


int edfwrite_physical_samples(int handle, double *buf);

/* Writes n physical samples (uV, mA, Ohm) from *buf belonging to one signal */
/* where n is the samplefrequency of the signal. */
/* The physical samples will be converted to digital samples using the */
/* values of physical maximum, physical minimum, digital maximum and digital minimum */
/* The number of samples written is equal to the samplefrequency of the signal */
/* Size of buf should be equal to or bigger than sizeof(double[samplefrequency]) */
/* Call this function for every signal in the file. The order is important! */
/* When there are 4 signals in the file,  the order of calling this function */
/* must be: signal 0, signal 1, signal 2, signal 3, signal 0, signal 1, signal 2, etc. */
/* Returns 0 on success, otherwise -1 */


int edf_blockwrite_physical_samples(int handle, double *buf);

/* Writes physical samples (uV, mA, Ohm) from *buf */
/* buf must be filled with samples from all signals, starting with n samples of signal 0, n samples of signal 1, n samples of signal 2, etc. */
/* where n is the samplefrequency of the signal. */
/* buf must be filled with samples from all signals, starting with signal 0, 1, 2, etc. */
/* one block equals one second */
/* The physical samples will be converted to digital samples using the */
/* values of physical maximum, physical minimum, digital maximum and digital minimum */
/* The number of samples written is equal to the sum of the samplefrequencies of all signals */
/* Size of buf should be equal to or bigger than sizeof(double) multiplied by the sum of the samplefrequencies of all signals */
/* Returns 0 on success, otherwise -1 */


int edfwrite_digital_samples(int handle, int *buf);

/* Writes n "raw" digital samples from *buf belonging to one signal */
/* where n is the samplefrequency of the signal. */
/* The 16 (or 24 in case of BDF) least significant bits of the sample will be written to the */
/* file without any conversion. */
/* The number of samples written is equal to the samplefrequency of the signal */
/* Size of buf should be equal to or bigger than sizeof(int[samplefrequency]) */
/* Call this function for every signal in the file. The order is important! */
/* When there are 4 signals in the file,  the order of calling this function */
/* must be: signal 0, signal 1, signal 2, signal 3, signal 0, signal 1, signal 2, etc. */
/* Returns 0 on success, otherwise -1 */


int edf_blockwrite_digital_samples(int handle, int *buf);

/* Writes "raw" digital samples from *buf. */
/* buf must be filled with samples from all signals, starting with n samples of signal 0, n samples of signal 1, n samples of signal 2, etc. */
/* where n is the samplefrequency of the signal. */
/* One block equals one second. */
/* The 16 (or 24 in case of BDF) least significant bits of the sample will be written to the */
/* file without any conversion. */
/* The number of samples written is equal to the sum of the samplefrequencies of all signals. */
/* Size of buf should be equal to or bigger than sizeof(int) multiplied by the sum of the samplefrequencies of all signals */
/* Returns 0 on success, otherwise -1 */


int edfwrite_annotation_utf8(int handle, long long onset, long long duration, const char *description);

/* writes an annotation/event to the file */
/* onset is relative to the starttime and startdate of the file */
/* onset and duration are in units of 100 microSeconds!     resolution is 0.0001 second! */
/* for example: 34.071 seconds must be written as 340710 */
/* if duration is unknown or not applicable: set a negative number (-1) */
/* description is a null-terminated UTF8-string containing the text that describes the event */
/* This function is optional and can be called only after opening a file in writemode */
/* and before closing the file */


int edfwrite_annotation_latin1(int handle, long long onset, long long duration, const char *description);

/* writes an annotation/event to the file */
/* onset is relative to the starttime and startdate of the file */
/* onset and duration are in units of 100 microSeconds!     resolution is 0.0001 second! */
/* for example: 34.071 seconds must be written as 340710 */
/* if duration is unknown or not applicable: set a negative number (-1) */
/* description is a null-terminated Latin1-string containing the text that describes the event */
/* This function is optional and can be called only after opening a file in writemode */
/* and before closing the file */


int edf_set_datarecord_duration(int handle, int duration);

/* Sets the datarecord duration. The default value is 1 second. */
/* ATTENTION: the argument "duration" is expressed in units of 10 microSeconds! */
/* So, if you want to set the datarecord duration to 0.1 second, you must give */
/* the argument "duration" a value of "10000". */
/* This function is optional, normally you don't need to change the default value. */
/* The datarecord duration must be in the range 0.025 to 20.0 seconds. */
/* Returns 0 on success, otherwise -1 */
/* This function is NOT REQUIRED but can be called after opening a */
/* file in writemode and before the first sample write action. */
/* This function can be used when you want to use a samplerate */
/* which is not an integer. For example, if you want to use a samplerate of 0.5 Hz, */
/* set the samplefrequency to 5 Hz and the datarecord duration to 10 seconds, */
/* or set the samplefrequency to 1 Hz and the datarecord duration to 2 seconds. */
/* Do not use this function, except when absolutely necessary! */


#ifdef __cplusplus
} /* extern "C" */
#endif

#endif






