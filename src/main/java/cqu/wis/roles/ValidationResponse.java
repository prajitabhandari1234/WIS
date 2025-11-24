/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cqu.wis.roles;

/**
 * Immutable record representing the result of a validation check. Contains a
 * boolean flag indicating validity and a message (error or informational).
 *
 * @param valid {@code true} if validation passed; {@code false} otherwise
 * @param message an informational or error message (empty if valid, or
 * "DEFAULT" for default‐password special case)
 *
 * @author Prajita Bhandari
 *
 */
public record ValidationResponse(boolean valid, String message) {

}
