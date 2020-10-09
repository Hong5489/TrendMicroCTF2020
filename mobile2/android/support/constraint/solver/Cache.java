package android.support.constraint.solver;

public class Cache {
  Pools.Pool<ArrayRow> arrayRowPool = new Pools.SimplePool<>(256);
  
  SolverVariable[] mIndexedVariables = new SolverVariable[32];
  
  Pools.Pool<SolverVariable> solverVariablePool = new Pools.SimplePool<>(256);
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/constraint/solver/Cache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */