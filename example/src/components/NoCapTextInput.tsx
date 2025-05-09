import {
  TextInput,
  StyleSheet,
  type TextInputProps,
  type TextStyle,
} from "react-native";

interface NoCapTextInputProps extends TextInputProps {
  style?: TextStyle | TextStyle[];
}

export default function NoCapTextInput({
  style,
  autoCapitalize = "none",
  ...props
}: NoCapTextInputProps) {
  return (
    <TextInput
      {...props}
      style={[styles.input, style]}
      autoCapitalize={autoCapitalize}
    />
  );
}

const styles = StyleSheet.create({
  input: {
    height: 40,
    borderColor: "#ccc",
    borderWidth: 1,
    borderRadius: 8,
    paddingHorizontal: 12,
    marginBottom: 12,
    textAlign: "center",
    backgroundColor: "#f9f9f9",
  },
});
