package org.hrchu.grebnetugwords;

/**
 * Created by hrchu on 2016/10/10.
 */
public class AskResult {
  public final long occur;
  public final long hit;

  public AskResult(GrebnetugWords.Tuple tuple) {
    this.occur = tuple.occur();
    this.hit = tuple.hit();
  }

  public AskResult(long occur, long hit) {
    this.occur = occur;
    this.hit = hit;
  }

  @Override
  public String toString() {
    return "[occur=" + occur + ", hit=" + hit + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AskResult askResult = (AskResult) o;

    if (occur != askResult.occur) {
      return false;
    } else {
      return hit == askResult.hit;
    }

  }

  @Override
  public int hashCode() {
    int result = (int) (occur ^ (occur >>> 32));
    result = 31 * result + (int) (hit ^ (hit >>> 32));
    return result;
  }
}
