// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.util.containers;

import com.intellij.openapi.util.SystemInfoRt;
import com.intellij.openapi.util.io.FileUtil;
import gnu.trove.TObjectHashingStrategy;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

// ContainerUtil requires trove in classpath
public final class CollectionFactory {
  private static final Hash.Strategy<File> FILE_HASH_STRATEGY = new Hash.Strategy<File>() {
    @Override
    public int hashCode(File o) {
      return FileUtil.fileHashCode(o);
    }

    @Override
    public boolean equals(File a, File b) {
      return FileUtil.filesEqual(a, b);
    }
  };

  @Contract(value = " -> new", pure = true)
  public static @NotNull <K, V> ConcurrentMap<K, V> createConcurrentWeakMap() {
    return new ConcurrentWeakHashMap<>(0.75f);
  }

  @Contract(value = " -> new", pure = true)
  public static @NotNull <K, V> ConcurrentMap<K, V> createConcurrentWeakIdentityMap() {
    //noinspection unchecked
    return new ConcurrentWeakHashMap<>((TObjectHashingStrategy<K>)TObjectHashingStrategy.IDENTITY);
  }

  @Contract(value = " -> new", pure = true)
  public static @NotNull <K, V> Map<K, V> createWeakMap() {
    return ContainerUtil.createWeakMap();
  }

  @Contract(value = " -> new", pure = true)
  public static @NotNull <K, V> ConcurrentMap<K, V> createConcurrentWeakKeyWeakValueMap() {
    return ContainerUtil.createConcurrentWeakKeyWeakValueMap(ContainerUtil.canonicalStrategy());
  }

  @Contract(value = "_,_,_ -> new", pure = true)
  public static @NotNull <K, V> ConcurrentMap<K, V> createConcurrentWeakMap(int initialCapacity,
                                                                            float loadFactor,
                                                                            int concurrencyLevel) {
    return new ConcurrentWeakHashMap<>(initialCapacity, loadFactor, concurrencyLevel, ContainerUtil.canonicalStrategy());
  }

  @Contract(value = " -> new", pure = true)
  public static @NotNull <K, V> ConcurrentMap<K, V> createConcurrentWeakKeySoftValueMap() {
    return ContainerUtil.createConcurrentWeakKeySoftValueMap();
  }

  public static @NotNull <T> Map<CharSequence, T> createCharSequenceMap(boolean caseSensitive, int expectedSize, float loadFactor) {
    return new Object2ObjectOpenCustomHashMap<>(expectedSize, loadFactor, FastUtilHashingStrategies.getCharSequenceStrategy(caseSensitive));
  }

  public static @NotNull Set<CharSequence> createCharSequenceSet(boolean caseSensitive, int expectedSize, float loadFactor) {
    return new ObjectOpenCustomHashSet<>(expectedSize, loadFactor, FastUtilHashingStrategies.getCharSequenceStrategy(caseSensitive));
  }

  public static @NotNull Set<CharSequence> createCharSequenceSet(boolean caseSensitive, int expectedSize) {
    return new ObjectOpenCustomHashSet<>(expectedSize, FastUtilHashingStrategies.getCharSequenceStrategy(caseSensitive));
  }

  public static @NotNull Set<CharSequence> createCharSequenceSet(boolean caseSensitive) {
    return new ObjectOpenCustomHashSet<>(FastUtilHashingStrategies.getCharSequenceStrategy(caseSensitive));
  }

  public static @NotNull <T> Map<CharSequence, T> createCharSequenceMap(boolean caseSensitive) {
    return new Object2ObjectOpenCustomHashMap<>(FastUtilHashingStrategies.getCharSequenceStrategy(caseSensitive));
  }

  public static @NotNull <T> Map<CharSequence, T> createCharSequenceMap(int capacity, float loadFactory, boolean caseSensitive) {
    return new Object2ObjectOpenCustomHashMap<>(capacity, loadFactory, FastUtilHashingStrategies.getCharSequenceStrategy(caseSensitive));
  }

  public static @NotNull Set<String> createCaseInsensitiveStringSet() {
    return new ObjectOpenCustomHashSet<>(FastUtilHashingStrategies.getCaseInsensitiveStringStrategy());
  }

  public static @NotNull Set<String> createCaseInsensitiveStringSet(@NotNull Set<String> items) {
    return new ObjectOpenCustomHashSet<>(items, FastUtilHashingStrategies.getCaseInsensitiveStringStrategy());
  }

  public static <V> @NotNull Map<String, V> createCaseInsensitiveStringMap() {
    return new Object2ObjectOpenCustomHashMap<>(FastUtilHashingStrategies.getCaseInsensitiveStringStrategy());
  }

  public static @NotNull Set<String> createFilePathSet() {
    if (SystemInfoRt.isFileSystemCaseSensitive) {
      return new HashSet<>();
    }
    else {
      return createCaseInsensitiveStringSet();
    }
  }

  public static @NotNull Set<String> createFilePathSet(int expectedSize) {
    return createFilePathSet(expectedSize, SystemInfoRt.isFileSystemCaseSensitive);
  }

  public static @NotNull Set<String> createFilePathSet(int expectedSize, boolean isFileSystemCaseSensitive) {
    if (isFileSystemCaseSensitive) {
      return new HashSet<>(expectedSize);
    }
    else {
      return new ObjectOpenCustomHashSet<>(expectedSize, FastUtilHashingStrategies.getCaseInsensitiveStringStrategy());
    }
  }

  public static @NotNull Set<String> createFilePathSet(@NotNull Collection<String> paths, boolean isFileSystemCaseSensitive) {
    if (isFileSystemCaseSensitive) {
      return new HashSet<>(paths);
    }
    else {
      return new ObjectOpenCustomHashSet<>(paths, FastUtilHashingStrategies.getCaseInsensitiveStringStrategy());
    }
  }

  public static @NotNull Set<String> createFilePathSet(String @NotNull[] paths, boolean isFileSystemCaseSensitive) {
    if (isFileSystemCaseSensitive) {
      return new HashSet<>(Arrays.asList(paths));
    }
    else {
      return new ObjectOpenCustomHashSet<>(paths, FastUtilHashingStrategies.getCaseInsensitiveStringStrategy());
    }
  }

  public static @NotNull Set<String> createFilePathSet(@NotNull Collection<String> paths) {
    return createFilePathSet(paths, SystemInfoRt.isFileSystemCaseSensitive);
  }

  public static @NotNull <V> Map<String, V> createFilePathMap() {
    if (SystemInfoRt.isFileSystemCaseSensitive) {
      return new HashMap<>();
    }
    else {
      return createCaseInsensitiveStringMap();
    }
  }

  public static @NotNull <V> Map<String, V> createFilePathMap(int expectedSize) {
    return createFilePathMap(expectedSize, SystemInfoRt.isFileSystemCaseSensitive);
  }

  public static @NotNull <V> Map<String, V> createFilePathMap(int expectedSize, boolean isFileSystemCaseSensitive) {
    if (isFileSystemCaseSensitive) {
      return new HashMap<>(expectedSize);
    }
    else {
      return new Object2ObjectOpenCustomHashMap<>(expectedSize, FastUtilHashingStrategies.getCaseInsensitiveStringStrategy());
    }
  }

  public static @NotNull <V> Map<File, V> createFileMap() {
    return new Object2ObjectOpenCustomHashMap<>(FILE_HASH_STRATEGY);
  }

  public static @NotNull Set<File> createFileSet() {
    return new ObjectOpenCustomHashSet<>(FILE_HASH_STRATEGY);
  }

  public static @NotNull Set<File> createFileLinkedSet() {
    return new ObjectLinkedOpenCustomHashSet<>(FILE_HASH_STRATEGY);
  }

  public static @NotNull Set<String> createFilePathLinkedSet() {
    if (SystemInfoRt.isFileSystemCaseSensitive) {
      return new ObjectLinkedOpenHashSet<>();
    }
    else {
      return new ObjectLinkedOpenCustomHashSet<>(FastUtilHashingStrategies.getCaseInsensitiveStringStrategy());
    }
  }

  /**
   * Create linked map with key hash strategy according to file system path case sensitivity.
   */
  public static @NotNull <V> Map<String, V> createFilePathLinkedMap() {
    if (SystemInfoRt.isFileSystemCaseSensitive) {
      return createSmallMemoryFootprintLinkedMap();
    }
    else {
      return new Object2ObjectLinkedOpenCustomHashMap<>(FastUtilHashingStrategies.getCaseInsensitiveStringStrategy());
    }
  }

  /**
   * Create linked map with canonicalized key hash strategy.
   */
  public static @NotNull <V> Map<String, V> createCanonicalFilePathLinkedMap() {
    return new Object2ObjectLinkedOpenCustomHashMap<>(new Hash.Strategy<String>() {
      @Override
      public int hashCode(String value) {
        return FileUtil.pathHashCode(value);
      }

      @Override
      public boolean equals(String val1, String val2) {
        return FileUtil.pathsEqual(val1, val2);
      }
    });
  }

  /**
   * Create map with canonicalized key hash strategy.
   */
  public static @NotNull <V> Map<File, V> createCanonicalFileMap() {
    return new Object2ObjectOpenCustomHashMap<>(new Hash.Strategy<File>() {
      @Override
      public int hashCode(File value) {
        return FileUtil.fileHashCode(value);
      }

      @Override
      public boolean equals(File val1, File val2) {
        return FileUtil.filesEqual(val1, val2);
      }
    });
  }

  /**
   * @return Map implementation with slightly faster access for very big maps (>100K keys) and a bit smaller memory footprint than {@link LinkedHashMap} and with predictable iteration order.
   * Null keys and values are permitted.
   * Use sparingly only when performance considerations are utterly important; in all other cases please prefer {@link LinkedHashMap}.
   * @see LinkedHashMap
   */
  @Contract(value = "-> new", pure = true)
  public static <K, V> @NotNull Map<K, V> createSmallMemoryFootprintLinkedMap() {
    //noinspection SSBasedInspection
    return new Object2ObjectLinkedOpenHashMap<>();
  }

  /**
   * @return Map implementation with slightly faster access for very big maps (>100K keys) and a bit smaller memory footprint than {@link HashMap}.
   * Null keys and values are permitted.
   * Use sparingly only when performance considerations are utterly important; in all other cases please prefer {@link HashMap}.
   */
  @Contract(value = "-> new", pure = true)
  public static <K, V> @NotNull Map<K, V> createSmallMemoryFootprintMap() {
    //noinspection SSBasedInspection
    return new Object2ObjectOpenHashMap<>();
  }

  /**
   * @return Map implementation with slightly faster access for very big maps (>100K keys) and a bit smaller memory footprint than {@link HashMap}.
   * Null keys and values are permitted.
   * Use sparingly only when performance considerations are utterly important; in all other cases please prefer {@link HashMap}.
   */
  @Contract(value = "_ -> new", pure = true)
  public static <K, V> @NotNull Map<K, V> createSmallMemoryFootprintMap(int expected) {
    //noinspection SSBasedInspection
    return new Object2ObjectOpenHashMap<>(expected);
  }

  /**
   * @return Map implementation with slightly faster access for very big maps (>100K keys) and a bit smaller memory footprint than {@link HashMap}.
   * Null keys and values are permitted.
   * Use sparingly only when performance considerations are utterly important; in all other cases please prefer {@link HashMap}.
   */
  @Contract(value = "_ -> new", pure = true)
  public static <K, V> @NotNull Map<K, V> createSmallMemoryFootprintMap(@NotNull Map<? extends K, ? extends V> map) {
    //noinspection SSBasedInspection
    return new Object2ObjectOpenHashMap<>(map);
  }

  /**
   * @return Map implementation with slightly faster access for very big maps (>100K keys) and a bit smaller memory footprint than {@link HashMap}.
   * Null keys and values are permitted.
   * Use sparingly only when performance considerations are utterly important; in all other cases please prefer {@link HashMap}.
   */
  @Contract(value = "_,_ -> new", pure = true)
  public static <K, V> @NotNull Map<K, V> createSmallMemoryFootprintMap(int expected, float loadFactor) {
    //noinspection SSBasedInspection
    return new Object2ObjectOpenHashMap<>(expected, loadFactor);
  }

  /**
   * @return Set implementation with slightly faster access for very big maps (>100K keys) and a bit smaller memory footprint than {@link HashSet}.
   * Null keys are permitted.
   * Use sparingly only when performance considerations are utterly important; in all other cases please prefer {@link HashSet}.
   * @see HashSet
   */
  @Contract(value = "-> new", pure = true)
  public static <K> @NotNull Set<K> createSmallMemoryFootprintSet() {
    //noinspection SSBasedInspection
    return new ObjectOpenHashSet<>();
  }
  /**
   * @return Set implementation with slightly faster access for very big maps (>100K keys) and a bit smaller memory footprint than {@link HashSet}.
   * Null keys are permitted.
   * Use sparingly only when performance considerations are utterly important; in all other cases please prefer {@link HashSet}.
   * @see HashSet
   */
  @Contract(value = "_-> new", pure = true)
  public static <K> @NotNull Set<K> createSmallMemoryFootprintSet(int expected) {
    //noinspection SSBasedInspection
    return new ObjectOpenHashSet<>(expected);
  }
  /**
   * @return Set implementation with slightly faster access for very big maps (>100K keys) and a bit smaller memory footprint than {@link HashSet}.
   * Null keys are permitted.
   * Use sparingly only when performance considerations are utterly important; in all other cases please prefer {@link HashSet}.
   * @see HashSet
   */
  @Contract(value = "_-> new", pure = true)
  public static <K> @NotNull Set<K> createSmallMemoryFootprintSet(@NotNull Collection<? extends K> collection) {
    //noinspection SSBasedInspection
    return new ObjectOpenHashSet<>(collection);
  }
}
