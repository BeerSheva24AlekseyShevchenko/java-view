package telran.view;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {
	String readString(String prompt);

	void writeString(String str);

	default void writeLine(Object obj) {
		writeString(obj.toString() + "\n");
	}

	default <T> T readObject(String prompt, String errorPrompt, Function<String, T> mapper) {
		boolean running = false;
		T res = null;

		do {
			running = false;
			try {
				String strRes = readString(prompt);
				res = mapper.apply(strRes);
			} catch (Exception e) {
				writeLine(errorPrompt + ": " + e.getMessage());
				running = true;
			}
		} while (running);

		return res;
	}

	default Integer readInt(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Integer::parseInt);
	}

	default Long readLong(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Long::parseLong);
	}

	default Double readDouble(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Double::parseDouble);
	}

	default Double readNumberRange(String prompt, String errorPrompt, double min, double max) {
		return readObject(prompt, errorPrompt, i -> {
			Double value = Double.parseDouble(i);
			if (value < min || value > max) {
				throw new IllegalArgumentException(
						String.format("The number must be in the range from %f to %f", min, max));
			}
			return value;
		});
	}

	default String readStringPredicate(String prompt, String errorPrompt,
			Predicate<String> predicate) {
		return readObject(prompt, errorPrompt, i -> {
			if (predicate.negate().test(i)) {
				throw new IllegalArgumentException("Invalid format");
			}

			return i;
		});
	}

	default String readStringOptions(String prompt, String errorPrompt,
			HashSet<String> options) {
		return readStringPredicate(prompt, errorPrompt, i -> {
			if (!options.contains(i)) {
				throw new IllegalArgumentException("The value could be: " + String.join(", ", options));
			}
			return true;
		});
	}

	default LocalDate readIsoDate(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, LocalDate::parse);
	}

	default LocalDate readIsoDateRange(String prompt, String errorPrompt, LocalDate from,
			LocalDate to) {
		return readObject(prompt, errorPrompt, i -> {
			LocalDate date = LocalDate.parse(i);
			if (date.isBefore(from) || date.isAfter(to)) {
				throw new IllegalArgumentException(
						String.format("The date must be in the range from %tF to %tF", from, to));
			}
			return date;
		});
	}
}
