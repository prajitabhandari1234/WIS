/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cqu.wis.roles;

/**
 *
 * @author acer
 */
/**
 * A simple record to encapsulate validation results.
 * 
 * @param result  true if validation passes; false otherwise
 * @param message an optional message for feedback (e.g., error details)
 */
public record ValidationResponse(boolean result, String message) {
}
