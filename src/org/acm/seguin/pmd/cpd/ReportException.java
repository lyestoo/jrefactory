package org.acm.seguin.pmd.cpd;


public class ReportException extends Exception {

    private Throwable cause;

  public ReportException(Throwable cause) {
    super();
      this.cause = cause;
  }
}
