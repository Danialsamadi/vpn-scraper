# VPN Config Scraper

## Overview

VPN Config Scraper is a Java Swing application designed to fetch and display VPN configurations from a provided URL. The application supports different VPN protocols such as VMess, VLess, Trojan, and Shadowsocks. The user interface includes tabs for each protocol, allowing for organized viewing and management of configurations.

## Features

- Fetches VPN configurations from a specified URL.
- Displays configurations in separate tabs for VMess, VLess, Trojan, and Shadowsocks.
- Supports copying configurations to the clipboard.
- Includes a countdown timer for auto-refreshing configurations.
- Menu options for fetching configurations and viewing developer information.

## Prerequisites

- Java Development Kit (JDK) 11 or higher.
- A compatible Integrated Development Environment (IDE) such as IntelliJ IDEA.

## Installation

1. **Clone the repository:**

    ```bash
    git clone https://github.com/your-username/vpn-config-scraper.git
    cd vpn-config-scraper
    ```

2. **Open the project in your IDE:**

    - If using IntelliJ IDEA, open the project directory and the IDE should automatically detect the project settings.

3. **Configure the project:**

    - Ensure that the JDK is set up correctly.
    - Add any additional libraries or dependencies if necessary.

## Usage

1. **Run the application:**

    - Use your IDE to run the `VPNConfigScraperFrame` class or compile and run the project from the command line.

2. **Fetch Configurations:**

    - Enter the URL of the VPN configuration file in the text field.
    - Click the "Fetch and Show Configs" button to load the configurations.

3. **View Configurations:**

    - Use the tabs to view configurations for VMess, VLess, Trojan, and Shadowsocks.
    - Click "Copy" next to a configuration to copy it to the clipboard.

4. **Menu Options:**

    - **Configs**: Fetch configurations manually.
    - **About Me**: View information about the developer.

## Example

1. Set the URL to: `https://raw.githubusercontent.com/ALIILAPRO/v2rayNG-Config/main/server.txt`
2. Click "Fetch and Show Configs."
3. View and manage the configurations in the tabs.

## Contribution

Contributions are welcome! Please fork the repository and submit a pull request with your changes. For any issues or suggestions, create an issue on the GitHub repository.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


